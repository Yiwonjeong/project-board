let btnPostCode = document.getElementById('btnPostCode');   // 우편번호
let addr1 = document.getElementById('addr1');               // 기본주소
let addr2 = document.getElementById('addr2');               // 상세주소
let inputObjId = document.getElementById('inputId');        // 아이디 입력란
let inputPass1 = document.getElementById('inputPass1');
let inputPass2 = document.getElementById('inputPass2');
let inputName = document.getElementById('inputName');
let inputBirth = document.getElementById('inputBirth');
let inputHp = document.getElementById('inputHp');
let btnRegister = document.getElementById('btnRegister');

// validation
let reUid   = /^[a-zA-Z0-9]+$/;
let rePass  = /^[a-zA-Z0-9]+$/;
let reName  = /^[a-zA-Z가-힣]+$/;
let reBirth = /^[0-9]{8}$/;
let reHp    = /^[0-9]{11}$/;



// validation msg
let check_name = document.getElementById("check_name");
let check_name2 = document.getElementById("check_name2");
let check_birth = document.getElementById("check_birth");
let check_birth2 = document.getElementById("check_birth2");
let check_ph = document.getElementById("check_ph");
let check_ph2 = document.getElementById("check_ph2");
let check_pw = document.getElementById("check_pw");
let check_pwc = document.getElementById("check_pwc");
let check_pw2 = document.getElementById("check_pw2");
let check_id1 = document.getElementById("check_id1");
let check_id2 = document.getElementById("check_id2");
let check_post = document.getElementById("check_post");



document.addEventListener('DOMContentLoaded', function() {
    console.log("DOMContent Register");

    // CRUD 버튼 이벤트 등록
    addEventListenerCRUDBtn();

    // 유효성 검사
    fnValidation();

});

/* 유효성 검사 */
function fnValidation() {

    // ID
    inputObjId.addEventListener("focusout", function(event){
        if(inputObjId.value == null) {
            check_id2.style.display = "";
            inputObjId.focus();
            btnRegister.removeAttribute('type');
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
        if(!reUid.test(inputObjId.value)){
            check_id1.style.display = "";
            inputObjId.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })

    // PASSWORD
    inputPass1.addEventListener("focusout", function(){
        if(inputPass1.value == null) {
            inputPass2.style.display = "";
            inputPass1.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
        if(!rePass.test(inputPass1.value)){
            check_pw.style.display = "";
            inputPass1.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })
    inputPass2.addEventListener("focusout", function(){
        if(inputPass1.value != inputPass2.value) {
            check_pwc.style.display = "";
            inputPass1.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })

    // NAME
    inputName.addEventListener("focusout", function(){
        if(inputName.value == null) {
            check_name.style.display = "";
            inputName.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
        if(!reName.test(inputName.value)){
            check_name2.style.display = "";
            inputName.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })

    // BIRTH
    inputBirth.addEventListener("focusout", function(){
        if(inputBirth.value == null) {
            check_birth.style.display = "";
            inputBirth.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
        if(!reBirth.test(inputBirth.value)){
            check_birth2.style.display = "";
            inputBirth.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })

    // HP
    inputHp.addEventListener("focusout", function(){
        if(inputHp.value == null) {
            check_ph.style.display = "";
            inputHp.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
        if(!reHp.test(inputHp.value)){
            check_ph2.style.display = "";
            inputHp.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })

    btnPostCode.addEventListener("focusout", function(){
        if(btnPostCode.value == null) {
            check_post.style.display = "";
            btnPostCode.focus();
            event.preventDefault(); // 폼 전송 막기
            return false;
        }
    })
}

/* 우편번호 API */
function fnPostCode(){
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
            console.log("postcode");
            console.log("zip : " + data.zonecode);
            console.log("address: " + data.address);

            btnPostCode.value = data.zonecode;
            addr1.value = data.address;
        }
    }).open();
}

function addEventListenerCRUDBtn() {
    // 여기에 CRUD 버튼에 대한 이벤트 리스너 추가
    btnPostCode.addEventListener("click", fnPostCode);
}
