package org.example.semiproject.gallery.service;

import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.gallery.domain.GalleryImage;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GalleryUploadService {

    // 첨부파일 저장위치
    @Value("${saveImgDir}") private String saveImgDir;
    @Value("${saveSimgDir}") private String saveSImgDir;

    public List<GalleryImage> processUpload(List<MultipartFile> ginames, int gno) {
        // 업로드 처리된 파일정보를 저장하기 위해 리스트 변수 선언
        List<GalleryImage> gis = new ArrayList<>();

        for (MultipartFile giname : ginames) {
            // 업로드할 파일 정보 알아내기 - 첨부파일명
            String fname = makeUUID() + giname.getOriginalFilename();

            // 업로드할 파일 정보 알아내기 - 파일 크기
            int fsize = (int) (giname.getSize() / 1024);

            // 첨부파일을 지정한 위치에 저장
            String savepath = saveImgDir + fname;

            try {
                // 임시폴더에 저장된 업로드 예정 파일을 지정한 위치에 저장
                giname.transferTo(new File(savepath));

                // 첨부파일 정보를 클래스객체로 만들어 리스트에 저장
                gis.add(GalleryImage.builder()
                        .imgsize(fsize).imgname(fname).gno(gno).build());

            } catch (IOException e) {
                log.error("첨부파일 처리중 오류발생!!");
                e.printStackTrace();
            }
        }
        return gis;
    }

    private String makeUUID() {
        String uuid = LocalDate.now() + "" + LocalTime.now();
        uuid = uuid.replace("-", "")
                .replace(":", "")
                .replace(".", "");

        return uuid;
    }

    public void makeThumbnail(String tfname, String basename) {
        // 업로드된 첫번째 이미지 파일 경로 설정
        // 원본 : abc123.jpg
        // 썸내일 : abc123_small.jpg

        String refname = saveImgDir + basename; // 원본이미지 경로
        String thumbname = saveSImgDir + tfname; // 썸네일이미지 경로

        // 썸내일 작업 진행
        try {
            // 원본이미지를 읽어서 메모리에 이미지객체(캔버스)를 생성
            BufferedImage img = ImageIO.read(new File(refname));

            // 이미지 크기와 crop 좌표 설정
            int imgW = Math.min(img.getHeight(), img.getWidth()) / 2;
            int imgH = imgW;

            // 지정한 위치를 기준으로 잘라냄
            // crop(대상, x좌표, y좌표, 잘라낼너비, 잘라낼높이, 투명도)
            BufferedImage scaleImg = Scalr.crop(img,
                    (img.getWidth() - imgW) / 2,
                    (img.getHeight() - imgH) / 2,  // crop할 좌표
                    imgW, imgH,   // crop할 이미지 크기
                    null);

            // 잘라낸 이미지를 330x350 크기로 재조정
            BufferedImage resizeImg = Scalr.resize(
                    scaleImg, 330, 350, null);

            // 재조정한 이미지를 실제 경로에 저장
            ImageIO.write(resizeImg, "png", new File(thumbname));

        } catch (Exception ex) {
            log.error("이미지 썸내일 작업중 오류발생!!!");
            ex.printStackTrace();
        }
    }

}