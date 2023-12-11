$(document).ready(function() {
    // 엔터 키 입력 감지
    $('.tdContentInput').on('keydown', function(event) {
        if (event.key === 'Enter') {
            // 새로운 input 요소 추가
            addNewInput();
        }
    });

    // 추가된 input 요소에도 같은 이벤트 적용
    $(document).on('keydown', '.tdContentInput', function(event) {
        if (event.key === 'Enter' || event.key === 'Tab') {
            // 새로운 input 요소 추가
            addNewInput();
        }
    });

    // 새로운 input 요소를 추가하는 함수
    function addNewInput() {
        // 현재 input 요소의 값 가져오기
        var inputValue = $('.tdContentInput:last').val();

        // 값이 비어있지 않을 경우에만 새로운 input 요소 추가
        if (inputValue.trim() !== '') {
            // 새로운 input 요소 생성 및 이전 input 아래에 추가
            var newInput = $('<input type="text" class="tdContentInput" placeholder="할일 추가(Enter 또는 Tab) / 최대 60자">');
            $('.tdContentInput:last').after(newInput);

            // 새로 추가된 input에 포커스 설정
            newInput.focus();
        }
    }
});

// JavaScript (또는 jQuery)
<!-- Add the 'active' class to the first 'nav-link' and set the initial border color -->
$(document).ready(function () {
    $('#todoBorder1').css('background-color', 'black');
    $('#todoBorder2').css('background-color', 'transparent');

    // Handle tab click
    $('.nav-link').click(function () {
        $('#todoBorder1').css('background-color', 'transparent');
        $('#todoBorder2').css('background-color', 'transparent');

        if ($(this).data('tab') === 'list') {
            $('#todoBorder1').css('background-color', 'black');
        } else {
            $('#todoBorder2').css('background-color', 'black');
        }
    });
});

