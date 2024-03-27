
// showing successfully deletion message
document.addEventListener('DOMContentLoaded', function () {
    const deleteSuccessToast = document.getElementById('deleteSuccessToast');
    setTimeout(function () {
        deleteSuccessToast.classList.add('show');

        // Set timeout to hide the toast after 2 seconds
        setTimeout(function () {
            deleteSuccessToast.classList.remove('show');

        }, 2500);  // 2000 milliseconds = 2.5 seconds (time the toast is visible)
    }, 300);  // 1000 milliseconds = 0.3 second (delay before showing the toast)
});

// showing unSuccessfully deletion message
document.addEventListener('DOMContentLoaded', function () {
    const deleteSuccessToast = document.getElementById('deleteUnSuccessToast');
    setTimeout(function () {
        deleteSuccessToast.classList.add('show');

        // Set timeout to hide the toast after 2 seconds
        setTimeout(function () {
            deleteSuccessToast.classList.remove('show');

        }, 3500);  // 2000 milliseconds = 3.5 seconds (time the toast is visible)
    }, 300);  // 1000 milliseconds = 0.3 second (delay before showing the toast)
});

// showing successfully create message
document.addEventListener('DOMContentLoaded', function () {
    const createSuccessToast = document.getElementById('createSuccessToast');
    setTimeout(function () {
        createSuccessToast.classList.add('show');

        // Set timeout to hide the toast after 2 seconds
        setTimeout(function () {
            createSuccessToast.classList.remove('show');

        }, 2500);  // 2500 milliseconds = 2.5 seconds (time the toast is visible)
    }, 300);  // 300 milliseconds = 0.3 second (delay before showing the toast)
});

// showing ticket successfully create message
document.addEventListener('DOMContentLoaded', function () {
    const createTicketSuccessToast = document.getElementById('createTicketSuccessToast');
    setTimeout(function () {
        createTicketSuccessToast.classList.add('show');

        // Set timeout to hide the toast after 2 seconds
        setTimeout(function () {
            createTicketSuccessToast.classList.remove('show');

        }, 2500);  // 2500 milliseconds = 2.5 seconds (time the toast is visible)
    }, 300);  // 300 milliseconds = 0.3 second (delay before showing the toast)
});

// showing successfully update message
document.addEventListener('DOMContentLoaded', function () {
    const updateSuccessToast = document.getElementById('updateSuccessToast');
    setTimeout(function () {
        updateSuccessToast.classList.add('show');

        // Set timeout to hide the toast after 2 seconds
        setTimeout(function () {
            updateSuccessToast.classList.remove('show');

        }, 2500);  // 2500 milliseconds = 2.5 seconds (time the toast is visible)
    }, 300);  // 300 milliseconds = 0.3 second (delay before showing the toast)
});