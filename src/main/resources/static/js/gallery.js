const galleryMessages = [
    '제목을 올바르게 입력하세요',
    '본문내용을 올바르게 입력하세요',
    '첨부할 이미지파일을 올바르게 선택하세요',
    '자동가입방지를 올바르게 클릭하세요',
];

// 모든 error-message 요소의 내용을 초기화
function clearErrorMessages() {
    document.querySelectorAll(".error-message")
        .forEach(error => error.textContent = '');
}

const displayErrorMessages = (input, message) => {
    let error = document.createElement('div');
    error.className = 'error-message';
    error.textContent = message;
    input.parentElement.appendChild(error);
}

const validGallery = (form) => {
    let isValid = true;

    // 로그인 폼안의 모든 input 요소 수집
    const inputs = form.querySelectorAll('input:not([readonly]), textarea:not([readonly])');
    inputs.forEach((input, idx) => {
        if (!input.checkValidity()) {
            displayErrorMessages(input, galleryMessages[idx]);
            isValid = false;
        }
    });
    const ggrecaptcha = grecaptcha.getResponse();
    if (ggrecaptcha === "") {
        const recaptcha = document.querySelector('#recaptcha');
        displayErrorMessages(recaptcha, galleryMessages[3]);
        isValid = false;
    }
    //console.log(ggrecaptcha);

    return isValid;
}

const submitGalleryfrm = async (frm) => {
    const formData = new FormData(frm);

    fetch('/api/v1/gallery/write', {
        method: 'POST',
        body: formData
    }).then(async response => {
        if (response.ok) {
            alert('게시글이 등록되었습니다!!');
            location.href = '/gallery/list';
        } else if (response.status === 400) {
            alert(await response.text());
        } else {
            alert('게시글 등록에 실패했습니다!! 다시 시도해 주세요!');
        }
    }).catch(error => {
        console.error('gallery write error:', error);
        alert('서버와 통신중 오류가 발생했습니다!! 관리자에게 문의하세요!');
    });
} // submitGalleryFrm