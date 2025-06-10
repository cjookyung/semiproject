package org.example.semiproject.gallery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.gallery.domain.Gallery;
import org.example.semiproject.gallery.domain.GalleryImage;
import org.example.semiproject.gallery.repository.GalleryImageRepository;
import org.example.semiproject.gallery.repository.GalleryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryMapper;
    private final GalleryImageRepository galleryImageMapper;
    private final GalleryUploadService galleryUploadService;

    @Transactional
    @Override
    public boolean newGalleryImage(Gallery gal, List<MultipartFile> ginames) {
        boolean result = false;

        // 작성한 게시글을 gallerys에 저장하고, 생성된 글번호를 알아냄
        int gno = -999;
        try {
            // jpa를 이용해서 방금 입력한 데이터의 id 추출
            Gallery newOne = galleryMapper.save(gal);
            gno = newOne.getGgno();
        } catch (Exception ex) {
            throw new IllegalStateException("insertGallery 오류발생!!");
        }

        // 첨부된 파일들을 업로드 처리하고
        // 알아낸 글번호로 첨부된 파일들에 대한 정보를 gallery_images에 저장
        List<GalleryImage> gis = null;
        if (!ginames.isEmpty()) {  // 첨부파일이 존재한다면
            // 업로드 처리후 업로드된 파일들의 정보를 리스트형태로 받아옴

            try {
                gis = galleryUploadService.processUpload(ginames, gno);
            } catch (Exception ex) {
                throw new IllegalStateException("processUpload 오류발생!!");
            }

            // 업로드된 파일의 정보를 gallery_images 테이블에 저장
            // 즉, 첨부된 파일정보를 개별 행으로 저장
            try {
                // jpa를 이용해서 List 형식의 데이터를 한번에 테이블에 저장
                galleryImageMapper.saveAll(gis);

            } catch (Exception e) {
                throw new IllegalStateException("insertGalleryImage 오류발생!!");
            }

            // 첨부된 파일들 중 첫번째 이미지 파일을 썸네일 처리
            try {
                galleryUploadService.makeThumbnail(
                        gal.getSimgname(), gis.get(0).getImgname());
            } catch (Exception ex) {
                throw new IllegalStateException("makeThumbnail 오류발생!!");
            }

            result = true;
        }

        return result;
    }

}