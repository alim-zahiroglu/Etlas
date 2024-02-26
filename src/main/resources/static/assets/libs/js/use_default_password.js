
$(document).ready(function () {
    // Get the elements
    const passwordInput = $('#password');
    const currentPasswordCheckbox = $('#useCurrentPassword');
    const defaultPasswordCheckbox = $('#useDefaultPassword');

    // Set the initial state
    if (currentPasswordCheckbox.is(':checked')) {
        passwordInput.val("").prop('readonly', true);
        defaultPasswordCheckbox.prop('checked', false);
    }

    // Handle checkbox change for currentPasswordCheckbox
    currentPasswordCheckbox.change(function () {
        if ($(this).is(':checked')) {
            passwordInput.val("").prop('readonly', true);
            defaultPasswordCheckbox.prop('checked', false);
        }
    });

    // Handle checkbox change for defaultPasswordCheckbox
    defaultPasswordCheckbox.change(function () {
        if ($(this).is(':checked')) {
            passwordInput.val('Etlas1234!').prop('readonly', true);
            currentPasswordCheckbox.prop('checked', false);
        } else {
            passwordInput.val("").prop('readonly', false);
        }
    });
});
