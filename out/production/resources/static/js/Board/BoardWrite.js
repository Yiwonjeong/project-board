"use strict";

let btnSave = document.getElementById("btnSave");                   // 저장 버튼

let oEditor = []; // 에디터 선언

/* Initialize */
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOMContent");
    /** 에디터 불러오기 */
    appendEditorHtml();

    /** CRUD 버튼 이벤트 등록 */
    addEventListenerCRUDBtn();
});


function fnSave() {

    let title = document.getElementById('title').value;                // 글 제목
    oEditor.getById["editorTxt"].exec("UPDATE_CONTENTS_FIELD", []);    // 에디터 글 내용 가져오기
    let content = document.getElementById("editorTxt").value;          // 글 내용
    let contentValidate =  oEditor.getById["editorTxt"].getContents();
    let fileUpload = document.getElementById("fileUpload");            // 파일 업로드
    let formData = new FormData();                                     //  파일 업로드를 위한 폼 생성

    let selectedFile = fileUpload.files[0];     // 업로드한 파일

    // validation
    if (title.trim() === "") {
        alert("제목을 입력해 주세요.");
        return;
    }

    // HTML 태그 및 공백 제거 후 내용 확인
    let strippedContent = contentValidate.replace(/[<][^>]*[>]/g, "").replace(/&nbsp;/g, "");

    if(strippedContent.trim() === "" || strippedContent == null) {
        console.log("strippedContent trim is empty");
        alert("내용을 입력해 주세요.");
        return;
    }

    let jsonData = {};

    if(!selectedFile) {
        console.log("파일없음");
        jsonData = {
            "title": title,
            "content": content
        };
    }else {
        console.log("파일있음");
        jsonData = {
            "title": title,
            "content": content,
            "name":selectedFile.name
        };
    }

    console.log("after jsonData: ", jsonData);


    if(confirm("등록하시겠습니까?")){

        ajaxAPI("/board/write", jsonData, "POST").then(response => {

            // 글 번호
            let no = response.no;

            // 파일 업로드
            console.log("selectedFile: " + selectedFile);

            if(selectedFile !== null && selectedFile !== undefined) {

                console.log("파일 있음2");
                formData.append("file", selectedFile);
                formData.append("oriName", selectedFile.name);


                $.ajax({
                    url: '/board/upload',
                    processData: false,
                    contentType: false,
                    data: formData,
                    type: 'POST',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function(result) {
                        console.log("result: " + result);
                         console.log("response.no: ", response.no);
                        // 글 보기 이동
                        let url = '/board/view?no=' + no;
                        window.location.href = url;
                    }
                })
            }else {
                console.log("파일 없음2");
                let url = '/board/view?no=' + no;
                window.location.href = url;
            }


        });

    }

}

function appendEditorHtml() {
    /** 에디터 설정 */
    // nhn.husky.EZCreator.createInIFrame 함수를 호출하여 에디터를 생성합니다.
    nhn.husky.EZCreator.createInIFrame({
        // 모드 변경기를 사용하지 않습니다. (WYSIWYG 및 HTML 모드를 전환할 수 있는 기능)
        bUseModeChanger: false,
        // 세로 리사이저를 사용하지 않습니다. (에디터 높이 조정 기능)
        bUseVerticalResizer: false,
        // 에디터의 객체 참조를 설정합니다.
        oAppRef: oEditor,
        // 에디터를 삽입할 영역의 ID를 지정합니다.
        elPlaceHolder: 'editorTxt',
        // 에디터의 스킨 파일 경로를 설정합니다.
        sSkinURI: '/smarteditor/SmartEditor2Skin.html',
        // 에디터의 생성자 함수를 지정합니다.
        fCreator: 'createSEditor2',
        htParams : {
            // 추가 글꼴 목록을 설정합니다.
            aAdditionalFontList : [["Noto Sans KR", "Noto Sans"]],
            // 기본 글꼴 설정을 지정합니다
            SE2M_FontName : {
                htMainFont: {'id': '돋움','name': '돋움', 'url': '','cssUrl': ''}
            },
            // 페이지 이동 시 확인창이 나오지 않도록 처리하는 함수를 설정합니다.
            fOnBeforeUnload : function() { }
        },
        // 에디터 로딩이 완료된 후 실행되는 콜백 함수입니다.
        fOnAppLoad: function() {
            // 'editorTxt' 영역에 기본 글꼴을 설정합니다. (글꼴: "Noto Sans KR", 크기: 11)
            oEditor.getById['editorTxt'].setDefaultFont( "돋움", 11);
            // 'editorTxt' 영역의 내용을 초기화합니다.
            oEditor.getById["editorTxt"].exec("SET_IR", [""]);
        }
    });
}

function addEventListenerCRUDBtn(){
    btnSave.addEventListener("click", fnSave);
}
