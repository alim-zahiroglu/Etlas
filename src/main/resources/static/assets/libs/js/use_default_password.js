$(document).ready(function () {
    // Get the elements
    var passwordInput = $('#password');
    var defaultPasswordCheckbox = $('#useDefaultPassword');

    // Set the initial state
    if (defaultPasswordCheckbox.is(':checked')) {
        passwordInput.val('Etlas1234!').prop('readonly', true);
    } else {
        passwordInput.val('').prop('readonly', false);
    }

    // Handle checkbox change
    defaultPasswordCheckbox.change(function () {
        if ($(this).is(':checked')) {
            passwordInput.val('Etlas1234!').prop('readonly', true);
        } else {
            passwordInput.val('').prop('readonly', false);
        }
    });
});

