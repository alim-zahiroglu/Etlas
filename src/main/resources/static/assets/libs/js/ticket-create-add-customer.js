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

        // if (lastName === '') {
        //     lastNameValidationMessage.innerHTML = '<li>Last name should not be blank</li>';
        //     isValidLastName = false;
        // } else {
        //     lastNameValidationMessage.innerHTML = '';
        // }
        //
        // if (lastName.length < 2 || lastName.length > 50) {
        //     lastNameValidationMessage.innerHTML += '<li>Last name must be 2~50 characters long</li>';
        //     isValidLastName = false;
        // }
        //
        // if (phone === '') {
        //     phoneValidationMessage.innerHTML = '<li>Phone should not be blank</li>';
        //     isValidPhone = false;
        // } else {
        //     phoneValidationMessage.innerHTML = '';
        // }
        //
        // if (phone.length < 10 || phone.length > 15) {
        //     phoneValidationMessage.innerHTML += '<li>Phone must be 10~15 digits long</li>';
        //     isValidPhone = false;
        // }

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
                // url: window.location.href,
                // data: newTicketData + '&' + newCustomerData,

                data: newCustomerData,
                success: function (response) {
                    // Handle success response
                    console.log("Customer saved successfully");
                    $('#ticketForm').submit();
                    // $('#add-new-customer-modal').modal('hide'); // Close the modal
                },
                error: function (error) {
                    // Handle error response
                    console.error("Error saving customer", error);
                }
            });
            // $('#ticketForm').submit();
        }

    });
});




    // Handle single ticket checkbox click
    $('#ticket-type-single').on('change', function () {
        if ($(this).prop('checked')) {
            // If single ticket is checked, uncheck the multiple ticket
            $('#ticket-type-multiple').prop('checked', false);
            setTicketAmountOneAndReadOnly();
            setSingleSelectMode();
        } else {
            $('#ticket-type-multiple').prop('checked', true);
            removeReadOnly();
            setMultipleSelectMode();
            // $('#mySelectForPassenger').val(null).trigger('change');
        }
    });

    // Handle multiple ticket checkbox click
    $('#ticket-type-multiple').on('change', function () {
        if ($(this).prop('checked')) {
            // If multiple ticket is checked, uncheck the single ticket
            $('#ticket-type-single').prop('checked', false);
            removeReadOnly();
            setMultipleSelectMode();
            // $('#mySelectForPassenger').val(null).trigger('change');
        } else {
            $('#ticket-type-single').prop('checked', true);
            setTicketAmountOneAndReadOnly();
            setSingleSelectMode();
        }
    });

    // Initial state
    if ($('#ticket-type-single').prop('checked')) {
        setTicketAmountOneAndReadOnly();
        setSingleSelectMode();
    } else if ($('#ticket-type-multiple').prop('checked')) {
        removeReadOnly();
        setMultipleSelectMode();
        // $('#mySelectForPassenger').val(null).trigger('change');
    }

// setup select2 dropdowns
$(document).ready(function () {
    function setupSelect2($select, $selectorArrow) {
        $select.select2({
            allowClear: false,
            closeOnSelect: true,
        });

        $select.on('select2:open', () => {
            $selectorArrow.removeClass('country-rotate-down').addClass('country-rotate-up');
        });

        $select.on('select2:close', () => {
            $selectorArrow.removeClass('country-rotate-up').addClass('country-rotate-down');
        });
    }

    const $mySelectForBoughtUser = $('#mySelectForBoughtUser');
    const $boughtUserSelectorArrow = $('#bought-user-selector-arrow');
    setupSelect2($mySelectForBoughtUser, $boughtUserSelectorArrow);

    const $mySelectForReceiver = $('#receiver-user');
    const $receiverSelectorArrow = $('#receiver-selector-arrow');
    setupSelect2($mySelectForReceiver, $receiverSelectorArrow);

    const $mySelectForAirLine = $('#mySelectForAirline');
    const $airlineSelectorArrow = $('#airline-selector-arrow');
    setupSelect2($mySelectForAirLine, $airlineSelectorArrow);

    // const $mySelectForFrom = $('#mySelectForFrom');
    // const $fromWhereSelectorArrow = $('#from-where-selector-arrow');
    // setupSelect2($mySelectForFrom, $fromWhereSelectorArrow);

    const $mySelectForTo = $('#mySelectForArrival');
    const $toWhereSelectorArrow = $('#to-where-selector-arrow');
    setupSelect2($mySelectForTo, $toWhereSelectorArrow);


    const $mySelectForPayer = $('#mySelectForPayer');
    const $payerSelectorArrow = $('#payer-selector-arrow');
    setupSelect2($mySelectForPayer, $payerSelectorArrow);

    const $mySelectForCountry = $('#mySelectForCountry');
    const $customerCountrySelector = $('#customer-country-selector');
    setupSelect2($mySelectForCountry, $customerCountrySelector);

    const $mySelectForPassenger = $('#mySelectForPassenger');
    const $passengerSelectorArrow = $('#passenger-selector-arrow');
    setupSelect2($mySelectForPassenger, $passengerSelectorArrow);

});


// initialize the dataRangePicker for perches-data and payed-data
$(function () {
    $('.date-picker').daterangepicker({
        showDropdowns: true,
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        locale: {
            format: 'DD/MM/YYYY' // Adjust the format to display only 24-hour time
        },
        // startDate: moment()
    });
});


// Add validation for ticket amount input
$('#inputTicketAmount').on('input', function () {
    const amount = $(this).val();
    if (amount < 1) {
        $(this).val(1);
    }
});

function setTicketAmountOneAndReadOnly() {
    $('#inputTicketAmount').val(1).prop('readonly', true);
}

function removeReadOnly() {
    $('#inputTicketAmount').prop('readonly', false);
}

function setSingleSelectMode() {
    $('#mySelectForPassenger').select2({
        multiple: false
    });
    $('#uploadTickets').prop('multiple', false).val('');
}

function setMultipleSelectMode() {
    $('#mySelectForPassenger').select2({
        multiple: true
    });
    $('#uploadTickets').prop('multiple', true);
}

// switch ticketType
$(document).ready(function () {
    // Handle one way checkbox click
    $('#type-one-way').on('change', function () {
        if ($(this).prop('checked')) {
            // If one way is checked, uncheck the round trip
            $('#type-round-trip').prop('checked', false);
            setSingleDatePickerForDeparture();
        } else {
            $('#type-round-trip').prop('checked', true);
            setMultipleDatePickerForDeparture();
        }
    });

    // Handle round trip checkbox click
    $('#type-round-trip').on('change', function () {
        if ($(this).prop('checked')) {
            // If round trip is checked, uncheck the one way
            $('#type-one-way').prop('checked', false);
            setMultipleDatePickerForDeparture();
        } else {
            $('#type-one-way').prop('checked', true);
            setSingleDatePickerForDeparture();
        }
    });

    // Initial state
    if ($('#type-one-way').prop('checked')) {
        setSingleDatePickerForDeparture();
    } else if ($('#type-round-trip').prop('checked')) {
        setMultipleDatePickerForDeparture();
    }
});

// Function to update the departure time label based on checkbox selection
function updateDepartureTimeLabel() {
    const isChecked = $('#type-one-way').prop('checked');
    const iconHtml = '<i class="fa-regular fa-calendar-days"></i>';

    const label = isChecked ? iconHtml + ' Departure time' : iconHtml + ' Departure time - Return time';
    $('#departure-time-label').html(label);
}

// Function to set the date picker for a single date
function setSingleDatePickerForDeparture() {
    $('#departure-time').daterangepicker({
        showDropdowns: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        locale: {
            format: 'DD/MM/YYYY HH:mm' // Adjust the format to display only 24-hour time
        },
        singleDatePicker: true,
        // startDate: moment()
    });

    updateDepartureTimeLabel();
}

// Function to set the date picker for a range of dates
function setMultipleDatePickerForDeparture() {
    $('#departure-time').daterangepicker({
        showDropdowns: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        locale: {
            format: 'DD/MM/YYYY HH:mm' // Adjust the format to display only 24-hour time
        },
        singleDatePicker: false,
        // startDate: moment()
    });

    updateDepartureTimeLabel();
}



// switch the value of the from-to selection
$('#from-to-switch-button').on('click', function() {
    const fromValue = $('#mySelectForFrom').val();
    const toValue = $('#mySelectForArrival').val();

    $('#mySelectForFrom').val(toValue).trigger('change');
    $('#mySelectForArrival').val(fromValue).trigger('change');
});


// switch the currency units
$(document).ready(function () {
    // Handle currency dropdown change for perchesPrice
    $('#perchesPrice .change-currency').on('click', function () {
        const currencySymbol = $(this).data('currency-symbol');
        const selectedCurrency = $(this).data('selected-currency');
        $('#perchesPrice .currency-symbol').text(currencySymbol);
        $('#salesPrice .currency-symbol').text(currencySymbol);
        $('#payedAmount .currency-symbol').text(currencySymbol);
        $('#selectedCurrencyUnitInput').val(selectedCurrency);
    });
});





