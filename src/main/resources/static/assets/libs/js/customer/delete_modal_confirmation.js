// Add an event listener for the delete button clicks
const deleteCustomerButtons = document.querySelectorAll('#delete-customer-row');
deleteCustomerButtons.forEach(button => {
    button.addEventListener('click', function () {
        const customer = this.getAttribute('data-customer');
        const customerId = this.getAttribute('data-customerId');
        showConfirmationModal(customer, customerId);
    });
});


function showConfirmationModal(customer, customerId) {
    // Disable body scroll when modal is open
    $('body').css('overflow', 'hidden');

    // Set the confirmation message with styled user
    const confirmationCustomerNameElement = document.getElementById('confirmationCustomerName');
    confirmationCustomerNameElement.innerHTML = `Are you sure you want to delete customer <strong style="color: red;">'${customer}'</strong>?`;

    // Show the confirmation modal
    const confirmationModal = $('#customerConfirmationModal').modal('show');

    // Add an event listener for the OK button in the modal
    document.getElementById('confirmDelete').addEventListener('click', function () {
        // Redirect to the delete URL
        window.location.href = `/customer/delete?customerId=${customerId}`;
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
    $('#customerConfirmationModal').modal('hide');
}
