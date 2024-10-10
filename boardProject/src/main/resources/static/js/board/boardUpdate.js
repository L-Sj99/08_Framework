// boardUpdate.html에 작성된 전역 변수들
// const imageList = /*[[${board.imageList}]]*/[];
// const previewList = document.querySelectorAll('img.preview'); 

// 기존에 존재하던 이미지의 순서(order)를 기록할 배열
const orderList = []; // 0, 1

// X 버튼이 눌려져 삭제되는 이미지의 순서(order)를 기록하는 Set
const deleteOrderList = new Set();
// Set : 중복된 값을 저장 못하게 하는 객체(Java Set이랑 똑같음)
// Set을 사용하는 이유 : x 버튼이 눌러질 때 마다 중복되는 값을 저장 못하게 하기 위해서

// input type="file" 태그들
const inputImageList = document.getElementsByClassName("inputImage");

// x 버튼들
const deleteImageList = document.getElementsByClassName("delete-image");

// 마지막으로 선택된 파일을 저장할 배열
const lastValidFiles = [null, null, null, null, null];

/** 미리보기 함수
 * @param {*} file : <input type = "file"> 에서 선택된 파일
 * @param {*} order : 이미지 순서
 */
const updatePreview = (file, order) => {
  // 선택된 파일이 지정된 크기를 초과한 경우 선택 막기
  const maxSize = 1024 * 1024 * 10; // 1MB 를 byte단위로 작성
  if (file.size > maxSize) { // 파일 크기 초과 시
    alert("10MB 이하의 이미지만 선택해주세요.");

    // 미리보기는 안되도 크기가 초과된 파일이 선택되어 있음
    if (lastValidFiles[order] === null) { // 이전 선택된 파일이 없는데 크기 초과 파일을 선택한 경우
      inputImageList[order].value = ""; // 선택 파일 삭제
      return;
    }
    // 이전에 선택된 파일이 있을 때
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(lastValidFiles[order]);
    inputImageList[order].files = dataTransfer.files;
    return;
  }
  // 선택된 이미지 백업
  lastValidFiles[order] = file;
  // JS에서 제공하는 파읽을 읽어오는 객체
  const reader = new FileReader();

  // 파일을 읽어 오는데 DataURL 형식으로 읽어옴
  // DataURL: 파일 전체 데이터가 브라우저가 해석할 수 있는 긴 주소형태 문자열로 변환
  reader.readAsDataURL(file);
  reader.addEventListener("load", e => {
    previewList[order].src = e.target.result; // e.target.result == 파일이 변환된 주소 형태 문자열

    // 이미지가 성공적으로 읽어진 경우 deleteOrderList에서 해당 이미지 순서를 삭제
    // 이유 : 이 전에 x 버튼을 눌러 삭제 기록이 있을 수 있기 때문에
    deleteOrderList.delete(order);
  })
}



/* input태그, x버튼에 이벤트 리스너 추가 */
for (let i = 0; i < inputImageList.length; i++) {

  // input 태그에 이미지 선택 시 미리보기 함수 호출
  inputImageList[i].addEventListener("change", e => {
    const file = e.target.files[0];

    if (file === undefined) { // 선택 취소 시

      // 이전에 선택한 파일이 없는 경우
      if (lastValidFiles[i] === null) return;

      // 이전에 선택한 파일이 "있을" 경우
      const dataTransfer = new DataTransfer();

      // DataTransfer가 가지고 있는 files 필드에 
      // lastValidFiles[i] 추가 
      dataTransfer.items.add(lastValidFiles[i]);

      // input의 files 변수에 lastVaildFile이 추가된 files 대입
      inputImageList[i].files = dataTransfer.files;

      // 이전 선택된 파일로 미리보기 되돌리기
      updatePreview(lastValidFiles[i], i); 
      return;
    }
    updatePreview(file, i);
  })

  /* X 버튼 클릭 시 미리보기, 선택된 파일 삭제 */
  deleteImageList[i].addEventListener("click", () => {
    previewList[i].src      = ""; // 미리보기 삭제
    inputImageList[i].value = ""; // 선택된 파일 삭제
    lastValidFiles[i]       = null; // 백업 파일 삭제

    // 기존에 존재하던 이미지가 있는 상태에서 x 버튼이 눌러졌을 때
    // -> 기존에 이미지가 있었는데 i번째 이미지 x 버튼 눌러서 삭제
    if(orderList.includes(i)) {
      deleteOrderList.add(i);
    }
  })
}

// --------------------------------------------------------------------------------------
/* 제목, 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardUpdateFrm");
form.addEventListener("submit", e => {
  const boardTitle = document.querySelector("[name=boardTitle]");
  const boardContent = document.querySelector("[name=boardContent]");
  if(boardTitle.value.trim().length === 0) {
    alert("제목을 작성해주세요.");
    boardTitle.focus();
    e.preventDefault();
    return;
  }
  if(boardContent.value.trim().length === 0) {
    alert("내용을 작성해주세요.");
    boardContent.focus();
    e.preventDefault();
    return;
  }
  // 제출 전 form태그 마지막 자식으로 input 추가 한 후 제출
  // -> 해당 input에는 삭제된 이미지 순서(deleteOrderList)가 담길 예정 == 추가
  const input = document.createElement("input");

  // Array.from() : Set -> Array로 변환
  // 배열을 value에 대입하면 자동으로 배열toString() 호출 -> [1, 2, 3] --> "1, 2, 3" 변환
  input.value = Array.from(deleteOrderList).toString();
  input.name = "deleteOrderList";
  input.type = "hidden";
  form.append(input); // 자식으로 input 추가

})


