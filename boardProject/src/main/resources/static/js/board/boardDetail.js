// 현재 상세 조회한 게시글 번호
const boardNo = location.pathname.split("/")[3];
/* 좋아요 하트 클릭 시 */
const boardLike = document.querySelector("#boardLike");
boardLike.addEventListener("click", e => {
  // 1. 로그인 여부 검사
  if(loginCheck === false) {
    alert("로그인 후 이용해 주세요.");
    return;
  }
  // 2. 비동기로 좋아요 요청
  fetch("/board/like", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : boardNo
  })
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("좋아요를 실패하였습니다.");
  })
  .then(result => {
    // console.log("result : ", result);

    // 좋아요 결과가 담긴 result 객체의 check 값에 따라 하트 아이콘을 비우기/채우기 지정
    if(result.check === 'insert') {
      boardLike.classList.add("fa-solid");
      boardLike.classList.remove("fa-regular");
    } else {
      boardLike.classList.remove("fa-solid");
      boardLike.classList.add("fa-regular");
    }
    // 좋아요 하트 다음 형제 요소의 내용을 result.count로 변경
    boardLike.nextElementSibling.innerText = result.count;
  })
  .catch(err => console.error(err));
})