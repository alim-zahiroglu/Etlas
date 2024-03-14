// document.addEventListener('DOMContentLoaded', function () {
//     document.getElementById('showAddCustomerModal').addEventListener('click', function () {
//         // Show the modal
//         const modal = new bootstrap.Modal(document.getElementById('add-new-customer-modal'));
//         modal.show();
//     });
// });


// document.addEventListener('DOMContentLoaded', function () {
//     // Check if there are any validation errors in the form
//     const form = document.getElementById('customerForm');
//     const validationErrors = form.querySelectorAll('.custom-invalid-feedback');
//
//     if (validationErrors.length > 0) {
//         // Show the modal
//         const modal = new bootstrap.Modal(document.getElementById('add-new-customer-modal'));
//         modal.show();
//     }
//
//     document.getElementById('showAddCustomerModal').addEventListener('click', function () {
//         // Show the modal
//         const modal = new bootstrap.Modal(document.getElementById('add-new-customer-modal'));
//         modal.show();
//     });
// });


document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('showAddCustomerModal').addEventListener('click', function () {
        // Show the modal
        const modal = new bootstrap.Modal(document.getElementById('add-new-customer-modal'));
        modal.show();
    });

    document.getElementById('saveCustomerButton').addEventListener('click', function (event) {
        // Prevent the form from being submitted
        event.preventDefault();

        // Validate the form fields
        let firstName = document.getElementById('inputFirstName').value.trim();
        let lastName = document.getElementById('inputLastName').value.trim();
        let phone = document.getElementById('inputPhone').value.trim();
        let firstNameValidationMessage = document.getElementById('firstNameValidationMessage');
        let lastNameValidationMessage = document.getElementById('lastNameValidationMessage');
        let phoneValidationMessage = document.getElementById('phoneValidationMessage');
        let isValidFirstName = true;
        let isValidLastName = true;
        let isValidPhone = true;

        if (firstName === '') {
            firstNameValidationMessage.innerHTML = '<li>First name should not be blank</li>';
            isValidFirstName = false;
        } else {
            firstNameValidationMessage.innerHTML = '';
        }

        if (firstName.length < 2 || firstName.length > 50) {
            firstNameValidationMessage.innerHTML += '<li>First name must be 2~50 characters long</li>';
            isValidFirstName = false;
        }

        if (lastName === '') {
            lastNameValidationMessage.innerHTML = '<li>Last name should not be blank</li>';
            isValidLastName = false;
        } else {
            lastNameValidationMessage.innerHTML = '';
        }

        if (lastName.length < 2 || lastName.length > 50) {
            lastNameValidationMessage.innerHTML += '<li>Last name must be 2~50 characters long</li>';
            isValidLastName = false;
        }

        if (phone === '') {
            phoneValidationMessage.innerHTML = '<li>Phone should not be blank</li>';
            isValidPhone = false;
        } else {
            phoneValidationMessage.innerHTML = '';
        }

        if (phone.length < 10 || phone.length > 15) {
            phoneValidationMessage.innerHTML += '<li>Phone must be 10~15 digits long</li>';
            isValidPhone = false;
        }

        // Add or remove error class based on validation result for first name
        const individualSection = document.getElementById('individualSection');
        if (!isValidFirstName) {
            individualSection.classList.add('error');
        } else {
            individualSection.classList.remove('error');
        }

        // Add or remove error class based on validation result for last name
        const lastNameSection = document.getElementById('lastNameSection');
        if (!isValidLastName) {
            lastNameSection.classList.add('error');
        } else {
            lastNameSection.classList.remove('error');
        }

        // Add or remove error class based on validation result for phone
        const phoneSection = document.getElementById('phoneSection');
        if (!isValidPhone) {
            phoneSection.classList.add('error');
        } else {
            phoneSection.classList.remove('error');
        }

        // If form is valid, submit the form
        if (isValidFirstName && isValidLastName && isValidPhone) {
            const newTicketData = $('#ticketForm').serialize();
            const newCustomerData = $('#customerForm').serialize();

            $.ajax({
                type: "POST",
                url: "/ticket/create-add-customer",
                data: newTicketData + '&' + newCustomerData,
                success: function (response) {
                    // Handle success response
                    console.log("Customer saved successfully");
                    $('#add-new-customer-modal').modal('hide'); // Close the modal
                },
                error: function (error) {
                    // Handle error response
                    console.error("Error saving customer", error);
                }
            });
        }

    });
});


// document.getElementById('saveCustomerButton').addEventListener('click', function (event) {
//     // Prevent the form from being submitted
//     event.preventDefault();
//
//     // Validate the first name field
//     let firstName = document.getElementById('inputFirstName').value.trim();
//     let firstNameValidationMessage = document.getElementById('firstNameValidationMessage');
//     let isValidFirstName = true;
//
//     if (firstName === '') {
//         firstNameValidationMessage.innerHTML = '<li>First name should not be blank</li>';
//         isValidFirstName = false;
//     } else {
//         firstNameValidationMessage.innerHTML = '';
//     }
//
//     if (firstName.length < 2 || firstName.length > 50) {
//         firstNameValidationMessage.innerHTML += '<li>First name must be 2~50 characters long</li>';
//         isValidFirstName = false;
//     }
//
//     // Add or remove error class based on validation result for first name
//     const individualSection = document.getElementById('individualSection');
//     if (!isValidFirstName) {
//         individualSection.classList.add('error');
//     } else {
//         individualSection.classList.remove('error');
//     }
//
//     // Validate the last name field
//     let lastName = document.getElementById('inputLastName').value.trim();
//     let lastNameValidationMessage = document.getElementById('lastNameValidationMessage');
//     let isValidLastName = true;
//
//     if (lastName === '') {
//         lastNameValidationMessage.innerHTML = '<li>Last name should not be blank</li>';
//         isValidLastName = false;
//     } else {
//         lastNameValidationMessage.innerHTML = '';
//     }
//
//     if (lastName.length < 2 || lastName.length > 50) {
//         lastNameValidationMessage.innerHTML += '<li>Last name must be 2~50 characters long</li>';
//         isValidLastName = false;
//     }
//
//     // Add or remove error class based on validation result for last name
//     const lastNameSection = document.getElementById('lastNameSection');
//     if (!isValidLastName) {
//         lastNameSection.classList.add('error');
//     } else {
//         lastNameSection.classList.remove('error');
//     }
//
//     // If both first name and last name are valid, submit the form
//     if (isValidFirstName && isValidLastName) {
//         const newTicketData = $('#ticketForm').serialize();
//         const newCustomerData = $('#customerForm').serialize();
//
//         $.ajax({
//             type: "POST",
//             url: "/ticket/create-add-customer",
//             data: newTicketData + '&' + newCustomerData,
//             success: function (response) {
//                 // Handle success response
//                 console.log("Customer saved successfully");
//                 $('#add-new-customer-modal').modal('hide'); // Close the modal
//             },
//             error: function (error) {
//                 // Handle error response
//                 console.error("Error saving customer", error);
//             }
//         });
//     }
//
// });













