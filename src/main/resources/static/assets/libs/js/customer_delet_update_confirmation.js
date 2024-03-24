// Add an event listener for the delete button clicks
const deleteButtons = document.querySelectorAll('#delete-row');
deleteButtons.forEach(button => {
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
    const confirmationModal = $('#confirmationModal').modal('show');

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
    $('#confirmationModal').modal('hide');
}



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