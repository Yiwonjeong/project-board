"use strict";

let no = document.getElementById("no");
let btnDelete = document.getElementById("btnDelete");                   // 삭제 버튼
let btnFileDownload = document.getElementById("fileDownload");          // 파일 다운로드 버튼
let textareaObjComment = document.getElementById("commentTxt");         // 댓글 작성 내용
//let divObjReply = document.getElementById("replyTxt");                  // 이미 작성된 댓글
//let textareaObjReply = document.getElementById("replyTxtModified");     // 댓글 수정 내용
let btnCoRegister = document.getElementById("btnCoRegister");           // 댓글 등록 버튼


/* Initialize */
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOMContent");

    /** CRUD 버튼 이벤트 등록 */
    addEventListenerCRUDBtn();
});

/* 글 삭제 */
function fnDelete(){

    let jsonData = {
        "no" : no.value
    };

    if (confirm("삭제하시겠습니까?")) {
        ajaxAPI("/board/delete", jsonData, "POST").then(response => {
            // 글 보기 이동
            window.location.href = "/";

        });
    }


}

/* 파일 다운로드 */
function fnFileDownload() {
    let fileNo = btnFileDownload.getAttribute("data-fno");

    ajaxAPI("/board/fileDownload?fno=" + fileNo, null, "GET").then(response => {
        console.log("fileDownload ajax success");
        window.location.href = '/board/fileDownload?fno=' + fileNo;
    });

}

/* 댓글 작성 */
function fnCoRegister() {

    let jsonData = {
        "no" : no.value,
        "comment" : textareaObjComment.value
    };

    ajaxAPI("/comment/write", jsonData, "POST").then(response => {
        window.location.href = '/board/view?no=' + no.value;
    });

}

/* 댓글 수정 */
function fnCoModify(cno) {

    let btnCoSave = document.getElementById("btnCoSave" + cno);     // 등록버튼
    let btnCoUpdate = document.getElementById("btnCoUpdate" + cno); // 수정버튼
    let btnCoCancel = document.getElementById("btnCoCancel" + cno); // 취소버튼
    let btnCoDelete = document.getElementById("btnCoDelete" + cno); // 삭제버튼
    let divObjReply = document.getElementById("replyTxt" + cno);    // 기존 댓글 내용
    let textareaObjReply = document.getElementById("replyTxtModified" + cno);   // 수정 댓글 내용

    // 버튼 style
    divObjReply.style.display = "none";
    btnCoUpdate.style.display = "none";
    btnCoDelete.style.display = "none";
    textareaObjReply.style.display = "";
    btnCoSave.style.display = "";
    btnCoCancel.style.display = "";

    // 수정 댓글 등록하기
    btnCoSave.addEventListener("click", function(){
        console.log("btnCoSave clicked: " + cno);
        console.log("기존 댓글: " + divObjReply.textContent);
        console.log("수정 댓글: " + textareaObjReply.value);

        let jsonData = {
            "cno" : cno,
            "comment" : textareaObjReply.value
        };

        ajaxAPI("/comment/update", jsonData, "POST").then(response => {
            console.log("update comment ajax success");
            window.location.href = '/board/view?no=' + no.value;
        });
    })

    // 댓글 수정 취소하기
    btnCoCancel.addEventListener("click", function(){
        divObjReply.style.display = "";
        btnCoUpdate.style.display = "";
        btnCoDelete.style.display = "";
        textareaObjReply.style.display = "none";
        btnCoSave.style.display = "none";
        btnCoCancel.style.display = "none";
        textareaObjReply.value = divObjReply.textContent;
    })

    // 댓글 삭제하기
    btnCoDelete.addEventListener("click", function(){

    });
}


/* 댓글 삭제 */
function fnCoDelete(cno) {
    console.log("fnCoDelete");
    console.log("cno: " + cno);

    if(confirm("댓글을 삭제하시겠습니까?")) {
        let jsonData = {
            "cno" : cno
        };

        ajaxAPI("/comment/delete", jsonData, "POST").then(response => {
            console.log("delete comment ajax success");
            window.location.href = '/board/view?no=' + no.value;
        });
    }
}

function addEventListenerCRUDBtn(){
    if(btnDelete != null) btnDelete.addEventListener("click", fnDelete);
    if(btnFileDownload != null) btnFileDownload.addEventListener("click", fnFileDownload);
    if(btnCoRegister != null) btnCoRegister.addEventListener("click", fnCoRegister);
}
