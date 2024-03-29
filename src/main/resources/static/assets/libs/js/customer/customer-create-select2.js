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

    const $countrySelect = $('#mySelectForCountry');
    const $countrySelectArrow = $('#customer-country-selector');
    setupSelect2($countrySelect, $countrySelectArrow);
});

$(document).ready(function () {
    // Handle individual checkbox click
    $('#type-individual').on('change', function () {
        if ($(this).prop('checked')) {
            // If individual is checked, uncheck the company
            $('#type-company').prop('checked', false);
            showIndividualSections();
        } else {
            $('#type-company').prop('checked', true);
            showCompanySections();
        }
    });

    // Handle company checkbox click
    $('#type-company').on('change', function () {
        if ($(this).prop('checked')) {
            // If company is checked, uncheck the individual
            $('#type-individual').prop('checked', false);
            showCompanySections();
        } else {
            $('#type-individual').prop('checked', true);
            showIndividualSections();
        }
    });

    // Initial state
    if ($('#type-individual').prop('checked')) {
        showIndividualSections();
    } else if ($('#type-company').prop('checked')) {
        showCompanySections();
    }
});

function showIndividualSections() {
    $('#companySection').hide();
    $('#officePhoneSection').hide();
    $('#individualSection').show();
    $('#lastNameSection').show();
    $('#genderSelection').show();
}

function showCompanySections() {
    $('#companySection').show();
    $('#officePhoneSection').show();
    $('#individualSection').hide();
    $('#lastNameSection').hide();
    $('#genderSelection').hide();
}