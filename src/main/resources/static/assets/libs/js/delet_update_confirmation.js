
    // Add an event listener for the delete button clicks
    const deleteButtons = document.querySelectorAll('.delete-row');
    deleteButtons.forEach(button => {
    button.addEventListener('click', function() {
        const user = this.getAttribute('data-user');
        const username = this.getAttribute('data-username');
        showConfirmationModal(user,username);
    });
});

    function showConfirmationModal(user,username) {
    $('body').css('overflow', 'hidden');

    document.getElementById('confirmationUsername').innerText = `Are you sure you want to delete user '${user}'?`;
    const confirmationModal = $('#confirmationModal').modal('show');

    // Add an event listener for the OK button in the modal
    document.getElementById('confirmDelete').addEventListener('click', function () {
    window.location.href = `/user/delete?username=${username}`;
});

    // Add an event listener for modal close events (when Cancel or close button is clicked)
    confirmationModal.on('hidden.bs.modal', function () {
    // Manually remove the modal-open class and modal-backdrop
    $('body').css('overflow', 'auto');
    document.getElementById('confirmDelete').removeEventListener('click', confirmDeleteAction);
});
}

    function closeConfirmationModal() {
    $('body').css('overflow', 'auto');
    $('#confirmationModal').modal('hide');
}


    // showing successfully deletion message
    document.addEventListener('DOMContentLoaded', function() {
    const successToast = document.getElementById('successToast');
    setTimeout(function() {
    successToast.classList.add('show');

    // Set timeout to hide the toast after 2 seconds
    setTimeout(function() {
    successToast.classList.remove('show');

}, 2000);  // 2000 milliseconds = 2 seconds (time the toast is visible)
}, 300);  // 1000 milliseconds = 1 second (delay before showing the toast)
});
