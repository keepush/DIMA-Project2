// 1. 즐겨찾기 변경 요청
function changeFavorite(vNumber) {
    // 이미 다른 즐겨찾기가 있는지 확인하기 위한 서버 요청
    fetch(`/user/favorite/status?vNumber=${vNumber}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // 서버 응답 확인
            const { alreadyFavorite, hasTopFavorite } = data;

            if (alreadyFavorite) {
                // 해당 항해가 등록되어 있는 경우, 변경 요청 X
                alert('이 항해는 이미 즐겨찾기로 등록되어 있습니다.');
                window.location.reload();
                return;
            }

            if (hasTopFavorite) {
                // 다른 항해가 설정되어 있는 경우, 확인을 누른 경우에만 변경 요청 전송
                if (confirm('이미 즐겨찾기로 등록된 항해가 있습니다. 변경하시겠습니까?')) {
                    updateFavorite(vNumber);
                } else {
                    window.location.reload();
                }
            } else {
                // 즐겨찾기가 설정되지 않은 경우, 변경 요청 전송
                updateFavorite(vNumber);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


// 2. 즐겨찾기 변경 함수
function updateFavorite(vNumber) {
    // 변경 요청 전송
    fetch(`/user/favorite?vNumber=${vNumber}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            'vNumber': vNumber
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('즐겨찾기가 변경되었습니다.');

                // 기존 즐겨찾기 해제, 새로운 즐겨찾기 설정
                const allFavoriteCheckboxes = document.querySelectorAll('input[name="favorite"]');

                // 기존 즐겨찾기 해제
                allFavoriteCheckboxes.forEach(checkbox => {
                    if (checkbox.value !== vNumber.toString() && checkbox.checked) {
                        checkbox.checked = false;
                    }
                });

                // 새로운 항목 선택 - 즐겨찾기 설정
                const currentCheckbox = document.querySelector(`input[name="favorite"][value="${vNumber}"]`);
                if (currentCheckbox) {
                    currentCheckbox.checked = true;
                }

                // 변경 사항 반영
                window.location.reload();

            } else {
                alert('즐겨찾기 변경에 실패하였습니다.');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}




// 3. 전체 선택
document.addEventListener('DOMContentLoaded', function () {
    const chooseAllCheckbox = document.getElementById('choose-all');
    const chooseOneCheckboxes = document.querySelectorAll('.choose-one');

    // 전체 선택 클릭 시, 개별 체크박스 상태 변경
    chooseAllCheckbox.addEventListener('change', function () {
        chooseOneCheckboxes.forEach(checkbox => {
            checkbox.checked = chooseAllCheckbox.checked;
        });
    });

    // 개별 체크박스 상태에 따른 전체 선택 체크박스 상태 변경
    chooseOneCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            if ([...chooseOneCheckboxes].every(cb => cb.checked)) {
                chooseAllCheckbox.checked = true;
            } else {
                chooseAllCheckbox.checked = false;
            }
        });
    });
});



// 4. 선택 항목 삭제
function confirmDelete() {
    const checkedBoxes = document.querySelectorAll('.choose-one:checked');

    // 선택 항목이 있고, 경고창 확인 시에만 삭제 실행
    if (checkedBoxes.length > 0) {
        const isConfirmed = confirm('삭제하시겠습니까?');
        if (isConfirmed) {
            const deleteForm = document.getElementById('delete-form');

            // 선택된 체크박스들만 유지하도록
            checkedBoxes.forEach(checkbox => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'vNumbers';
                input.value = checkbox.value;
                deleteForm.appendChild(input);
            });

            deleteForm.submit();
        }
    } else {
        alert('삭제할 항목을 선택해주세요.');
    }
}