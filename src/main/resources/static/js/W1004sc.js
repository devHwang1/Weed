
function generateEventId() {
    // 이벤트를 위한 고유한 ID를 생성하는 로직을 구현
    return Date.now().toString() + window.performance.now().toString().replace('.', '');
}

function getLoggedInMemberInfo() {
    //로그인된 정보 불러오기
    return new Promise(function(resolve, reject) {
        $.ajax({
            type: 'GET',
            url: '/api/current',
            success: function(response) {
                resolve(response)
            },
            error: function(error) {
                console.error('Error getting logged-in member ID:', error.responseText);
            }
        })
    });
}



document.addEventListener('DOMContentLoaded', function () {
    var info = {}; // info 변수를 빈 객체로 초기화

    var calendarEl = document.getElementById('calendar');
    var eventModal = document.getElementById('eventModal');
    var eventTitleInput = document.getElementById('eventTitle');
    var eventContentInput = document.getElementById('eventContent');

    //모달상세
    var eventModalDetail = document.getElementById('eventModalDetail');    //상세모달창
    var eventTitleDetail = document.getElementById('eventTitleDetail');
    var startDateDetail = document.getElementById('startDateDetail');
    var endDateDetail = document.getElementById('endDateDetail');
    var eventContentDetail = document.getElementById('eventContentDetail');

    //모달상세버튼
    var closeModalButtonDetail = document.getElementById('closeModalDetail');
    var deleteModalButtonDetail =document.getElementById('deleteModalDetail');

    //모달버튼
    var saveEventButton = document.getElementById('saveEvent');
    var closeModalButton = document.getElementById('closeModal');

    // 날짜인풋
    var startDateInput = document.getElementById('startDate');
    var endDateInput = document.getElementById('endDate');

    //사용자 지정 이벤트 색깔
    var selectedColor = "#7A9DA8"; //색깔 초기값설정
    var eventColorInput  = document.getElementById('eventcolor');

    var clickCount = 0;
    var timeout;

    // 서버로부터 업데이트된 전체 일정을 가져와 FullCalendar에 반영
    $.ajax({
        type: 'GET',
        url: '/api/events',
        success: function (response) {
            calendar.removeAllEvents(); // 기존 이벤트 제거
            calendar.addEventSource(response); // 새로운 이벤트 소스 추가
        },
        error: function (error) {
            console.error('Failed to fetch updated events:', error);
            // 오류 처리
        }
    });


    function showEventinfo(arg) {
        // 모달 창 나타내기
        eventModalDetail.classList.add('show');

        // 이벤트 정보 표시
        var eventTitle = arg.event.title;
        var eventContent = arg.event.extendedProps.content; // 이벤트 내용을 가져오기

        var eventStart = arg.event.start;
        var eventEnd = arg.event.end;

        //종료 시작일 날짜 설정
        if (eventStart) {
            startDateDetail.innerHTML = formatDate(eventStart);

            if (eventEnd) {
                if (eventStart.toDateString() === eventEnd.toDateString()) {
                    endDateInput.value = formatDate(eventEnd);
                    endDateDetail.innerHTML = formatDate(eventEnd);
                } else {
                    endDateInput.value = formatDate(eventEnd);
                    endDateDetail.innerHTML = formatDate(eventEnd);
                }
            } else {
                endDateInput.value = formatDate(eventStart);
                endDateDetail.innerHTML = formatDate(eventStart);
            }
        }


        //input 값 가져오기
        eventTitleInput.value = eventTitle;
        eventContentInput.value=eventContent;

        // 이벤트 정보를 특정 HTML 요소에 표시
        eventTitleDetail.innerHTML = eventTitle;       //제목
        eventContentDetail.innerHTML = eventContent;   //내용


        info.id = arg.event.id;

        // 모달 창의 높이 계산
        var modalHeight = eventModalDetail.clientHeight;

        // 클릭한 위치를 기준으로 모달창 위치 설정
        var modalLeft = arg.jsEvent.clientX;
        var modalTop = arg.jsEvent.clientY;

        // 모달창이 화면 하단을 넘어갈 경우 조정
        var screenHeight = window.innerHeight;

        if (modalTop + modalHeight > screenHeight) {
            modalTop = screenHeight - modalHeight;
        }

        eventModalDetail.style.left = modalLeft + 'px';
        eventModalDetail.style.top = modalTop + 'px';

        eventModalDetail.style.display = 'block';

    }

    var calendar = new FullCalendar.Calendar(calendarEl, {
        // 달력 설정
        headerToolbar: {
            left: 'prev',
            center: 'title',
            right: 'next today'
        },

        eventContent: function (info) {
            // 이벤트의 콘텐츠를 생성하는 함수
            var content = document.createElement('div');
            content.classList.add('event-content');

            // 이벤트의 제목과 기타 정보를 추가
            content.innerHTML = '<b>' + info.event.title + '</b><br>';

            // 추가적인 콘텐츠 설정이 필요한 경우 여기에 추가

            return { domNodes: [content] };
        },


        locale: "ko",   // 언어설정
        dayMaxEvents: 2, // 하나의 날짜에 표시할 수 있는 최대 이벤트 수


        // 날짜 '일' 없애기
        dayCellContent: function (info) {
            var number = document.createElement("a");
            number.classList.add("fc-daygrid-day-number");
            number.innerHTML = info.dayNumberText.replace("일", '');

            if (info.view.type === "dayGridMonth") {
                return {
                    html: number.outerHTML
                };
            }

            return {
                domNodes: []
            };
        },

        selectable: true,
        editable: true, // 일정 이동 및 기간수정 가능하도록 활성화
        eventResizableFromStart: true,


        select: function (info) {
            eventModal.classList.add('show');
            startDateInput.value = formatDate(info.start);
            endDateInput.value = formatDate(info.end);

            // select 이벤트에서 info.id 설정
            info.id = generateEventId();
            info.date = info.start;
            info.allDay = info.allDay;

        },



        // 날짜 더블클릭시 모달 표시
        dateClick: function (eventInfo) {
            clickCount++;

            info = eventInfo;

            // 더블 클릭에 따른 이벤트 처리
            if (clickCount === 1) {
                timeout = setTimeout(function () {
                    clickCount = 0; // 클릭 횟수 초기화
                }, 300);  // 300ms 내에 클릭하지 않으면 초기화
            } else if (clickCount === 2) {
                clearTimeout(timeout);
                clickCount = 0; // 클릭 횟수 초기화

                // 모달 창 나타내기
                eventModal.classList.add('show');

                // 더블클릭한 날짜를 시작일과 종료일로 설정
                var selectedDate = eventInfo.date;
                var startDate = new Date(selectedDate);

                var endDate = new Date(selectedDate);

                startDateInput.value = formatDate(startDate);
                endDateInput.value = formatDate(endDate);



                // 더블클릭 이벤트 처리
                eventTitleInput.value = ''; // 초기화
                eventContentInput.value='';

                // 클릭한 위치를 기준으로 모달창 위치 설정
                var modalLeft = eventInfo.jsEvent.clientX;
                var modalTop = eventInfo.jsEvent.clientY;

                // 모달창이 화면 하단을 넘어갈 경우 조정
                var screenHeight = window.innerHeight;
                var modalHeight = eventModal.clientHeight;

                if (modalTop + modalHeight > screenHeight) {
                    modalTop = screenHeight - modalHeight;
                }

                eventModal.style.left = modalLeft + 'px';
                eventModal.style.top = modalTop + 'px';

                eventModal.style.display = 'block';
            }
        },

        // 닫기 버튼을 눌렀을 때
        closeEventModal: function () {
            eventModal.style.display = 'none';
        },


        // 이벤트 클릭시 상세정보 보여주기
        eventClick: function (arg) {
            clickCount++;

            // 더블 클릭에 따른 이벤트 처리
            if (clickCount === 1) {
                timeout = setTimeout(function () {
                    clickCount = 0; // 클릭 횟수 초기화
                }, 300);  // 300ms 내에 클릭하지 않으면 초기화
            } else if (clickCount === 2) {
                clearTimeout(timeout);
                clickCount = 0; // 클릭 횟수 초기화

                showEventinfo(arg);
            }


        },
        eventResize: function (eventResizeInfo) {
            var resizedEvent = eventResizeInfo.event;
            var newEndDate = resizedEvent.end;

            console.log('이벤트가 리사이즈되었습니다.');
            console.log('새로운 종료 날짜:', newEndDate);},

    });



    // 저장 버튼을 눌렀을 때
    saveEventButton.addEventListener('click', function () {
        var title = eventTitleInput.value.trim();
        var content = eventContentInput.value.trim();
        if (!title) return; // 타이틀이 비어있으면 아무 동작도 하지 않음

        var startDate = new Date(startDateInput.value);
        var endDate = new Date(endDateInput.value);

        // 사용자가 선택한 색상 가져오기
        selectedColor = eventColorInput.value;

        // 이벤트 ID 생성
        var eventId = generateEventId();

        // AJAX를 이용하여 서버로 데이터 전송
        getLoggedInMemberInfo().then(function (memberInfo) {
            $.ajax({
                type: 'POST',
                url: '/saveW1004', // 실제 백엔드 서버의 URL에 맞게 조정
                contentType: 'application/json',
                data: JSON.stringify({
                    id: eventId,
                    title: title,
                    content: content,
                    startDate: startDate,
                    endDate: endDate,
                    color: selectedColor,
                    loggedInMemberId: memberInfo.id,
                    loggedInMemberName: memberInfo.name
                }),
                success: function (response) {
                    // 서버로부터의 응답 처리

                    var event = {
                        id: eventId,
                        title: title,
                        content: content,
                        start: startDate,
                        end: endDate,
                        color: selectedColor,
                        loggedInMemberName: memberInfo.name
                    };

                    // 저장된 이벤트만을 가져와 FullCalendar에 추가
                    calendar.addEvent({
                        id: eventId,
                        title: title,
                        content: content,
                        start: startDate,
                        end: endDate,
                        color: selectedColor,
                        loggedInMemberName: memberInfo.name
                    });


                    // 서버로부터 업데이트된 전체 일정을 가져와 FullCalendar에 반영
                    $.ajax({
                        type: 'GET',
                        url: '/api/events',
                        success: function (updatedEvents) {
                            // 기존 이벤트 제거
                            calendar.getEvents().forEach(function (existingEvent) {
                                existingEvent.remove();
                            });

                            // 새로운 이벤트 소스 추가
                            calendar.addEventSource(updatedEvents);
                        },
                        error: function (error) {
                            console.error('Failed to fetch updated events:', error);
                            // 오류 처리
                        }
                    });

                    // 여기에 성공적으로 저장되었을 때의 로직을 추가
                    console.log('Event saved successfully:', response);

                    // 유저네임을 저장하고 표시
                    var savedUsernameElement = document.getElementById('savedUsername'); // 변경 필요한 요소의 ID를 사용
                    if (savedUsernameElement) {
                        savedUsernameElement.innerText = memberInfo.name;
                    }
                },
                error: function (error) {
                    // 서버로부터의 응답에 에러가 있을 때 처리
                    console.error('Error saving event:', error.responseText);
                    // 여기에 에러 발생 시의 로직을 추가
                }
            });
        });


        calendar.unselect();
        eventModal.style.display = 'none';

    });

    // 삭제 버튼을 눌렀을 때 (상세)
    deleteModalButtonDetail.addEventListener('click', function () {
        console.log('Deleting event with ID:', info.id); // 추가된 로그

        if (confirm('이 일정을 삭제하시겠습니까?')) {
            // 현재 선택된 이벤트 객체 가져오기
            var selectedEvent = calendar.getEventById(info.id);

            // 이벤트가 있다면 삭제
            if (selectedEvent) {
                selectedEvent.remove();
            }

            // 모달 닫기
            eventModalDetail.style.display = 'none';
        }
    });


    // 취소 버튼을 눌렀을때 (작성 , 상세)
    //일정작성
    closeModalButton.addEventListener('click', function () {
        calendar.unselect();
        eventModal.style.display = 'none';

    });

    //일정상세
    closeModalButtonDetail.addEventListener('click', function () {
        calendar.unselect();
        eventModalDetail.style.display = 'none';

    });
    calendar.render();
});

// 문자열 변환
function formatDate(date) {
    // 날짜 객체가 유효한지 확인
    if (date instanceof Date && !isNaN(date)) {
        // 날짜를 "YYYY-MM-DD" 형식의 문자열로 변환
        var year = date.getFullYear();
        var month = String(date.getMonth() + 1).padStart(2, '0');
        var day = String(date.getDate()).padStart(2, '0');
        return year + '-' + month + '-' + day;
    } else {
        return ''; // 유효하지 않은 경우 빈 문자열 반환
    }
}