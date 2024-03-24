// Add an event listener for the delete button clicks
const deleteButtons = document.querySelectorAll('#delete-row');
deleteButtons.forEach(button => {
    button.addEventListener('click', function () {
        const user = this.getAttribute('data-user');
        const username = this.getAttribute('data-username');
        showConfirmationModal(user, username);
    });
});

function showConfirmationModal(user, username) {
    // Disable body scroll when modal is open
    $('body').css('overflow', 'hidden');

    // Set the confirmation message with styled user
    const confirmationUsernameElement = document.getElementById('confirmationUsername');
    confirmationUsernameElement.innerHTML = `Are you sure you want to delete user <strong style="color: red;">'${user}'</strong>?`;

    // Show the confirmation modal
    const confirmationModal = $('#confirmationModal').modal('show');

    // Add an event listener for the OK button in the modal
    document.getElementById('confirmDelete').addEventListener('click', function () {
        // Redirect to the delete URL
        window.location.href = `/user/delete?username=${username}`;
    });

    // Add an event listener for modal close events (when Cancel or close button is clicked)
    confirmationModal.on('hidden.bs.modal', function () {
        // Enable body scroll
        $('body').css('overflow', 'auto');

        // Remove the event listener to prevent memory leaks
        document.getElementById('confirmDelete').removeEventListener('click', confirmDeleteAction);
    });
}


function closeConfirmationModal() {
    $('body').css('overflow', 'auto');
    $('#confirmationModal').modal('hide');
}
