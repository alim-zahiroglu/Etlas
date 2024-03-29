// Add an event listener for the delete button clicks
const deleteButtons = document.querySelectorAll('#delete-row');
deleteButtons.forEach(button => {
    button.addEventListener('click', function () {
        const cardName = this.getAttribute('data-card');
        const cardId = this.getAttribute('data-cardId');
        const from = this.getAttribute('data-from');
        showConfirmationModal(cardName, cardId, from);
    });
});

function showConfirmationModal(cardName, cardId, from) {
    // Disable body scroll when modal is open
    $('body').css('overflow', 'hidden');

    // Set the confirmation message with styled user
    const confirmationUsernameElement = document.getElementById('confirmationCardName');
    confirmationUsernameElement.innerHTML = `Are you sure you want to delete card <strong style="color: red;">'${cardName}'</strong>?`;

    // Show the confirmation modal
    const confirmationModal = $('#confirmationModal').modal('show');

    // Add an event listener for the OK button in the modal
    document.getElementById('confirmDelete').addEventListener('click', function () {
        // Redirect to the delete URL
        window.location.href = `/card/delete?cardId=${cardId}&from=${from}`;

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


function addUpdateButtonEventListeners(buttonSelector) {
    const updateButtons = document.querySelectorAll(buttonSelector);
    updateButtons.forEach(button => {
        button.addEventListener('click', function () {
            const cardId = this.getAttribute('data-cardId');
            const from = this.getAttribute('data-from');
            goToUpdatePage(cardId, from);
        });
    });
}

function goToUpdatePage(cardId, from) {
    window.location.href = `/card/update/${cardId}?from=${from}`;
}

// Add event listeners for update buttons in the list view page
addUpdateButtonEventListeners('#cardUpdateList');

// Add event listeners for update buttons in the card view page
addUpdateButtonEventListeners('#cardUpdateCardList');
