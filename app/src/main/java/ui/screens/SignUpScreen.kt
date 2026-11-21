
package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SUBackground = Color(0xFFFAF8F3)
private val SUGreen = Color(0xFF176B52)
private val SUDark = Color(0xFF24303D)
private val SUGray = Color(0xFF718096)
private val SUBorder = Color(0xFFE7E5DF)
private val SUSuccessBackground = Color(0xFFE8F2EC)

@Composable
fun SignUpScreen(
    onSignUp: () -> Unit = {},
    onLogin: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var acceptedTerms by remember { mutableStateOf(false) }
    var emailOptIn by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var attempted by remember { mutableStateOf(false) }
    var confirmedEmail by remember { mutableStateOf("") }

    val normalizedEmail = email.trim()

    val nameValid = fullName.trim().isNotEmpty()

    val emailValid = Regex(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    ).matches(normalizedEmail)

    val emailSuggestion = when {
        normalizedEmail.endsWith(
            "@hotmail.co",
            ignoreCase = true
        ) -> normalizedEmail + "m"

        normalizedEmail.endsWith(
            "@gmail.co",
            ignoreCase = true
        ) -> normalizedEmail + "m"

        normalizedEmail.endsWith(
            "@outlook.co",
            ignoreCase = true
        ) -> normalizedEmail + "m"

        else -> null
    }

    val needsEmailConfirmation =
        emailSuggestion != null &&
                confirmedEmail != normalizedEmail

    val passwordValid =
        password.length >= 8 &&
                password.any { it.isDigit() }

    val nameError = attempted && !nameValid

    val emailError = attempted &&
            (!emailValid || needsEmailConfirmation)

    val passwordError = attempted && !passwordValid

    val termsError = attempted && !acceptedTerms

    val formValid =
        nameValid &&
                emailValid &&
                !needsEmailConfirmation &&
                passwordValid &&
                acceptedTerms

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SUBackground)
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // Top app bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = SUGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Create your account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SUDark
                )
            }

            Spacer(Modifier.height(38.dp))

            // Heading
            Text(
                text = "Make room for\ndiscovery.",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp,
                color = SUDark
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "A few details to make LocalLens yours.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = SUGray
            )

            Spacer(Modifier.height(28.dp))

            // Full name
            SignUpLabel("Full name")

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Noura Al Qahtani",
                        fontSize = 14.sp,
                        color = SUGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = "Name",
                        tint = SUGray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                isError = nameError,
                shape = RoundedCornerShape(12.dp),
                colors = signUpFieldColors()
            )

            if (nameError) {
                SignUpError("Full name is required")
            }

            Spacer(Modifier.height(18.dp))

            // Email address
            SignUpLabel("Email address")

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    confirmedEmail = ""
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "noura@example.com",
                        fontSize = 14.sp,
                        color = SUGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        tint = SUGray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                isError = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(12.dp),
                colors = signUpFieldColors()
            )

            if (attempted && !emailValid) {
                SignUpError("Enter a valid email address")
            } else if (
                attempted && needsEmailConfirmation
            ) {
                SignUpError("Please review your email address")
            }

            if (
                needsEmailConfirmation &&
                emailSuggestion != null
            ) {
                TextButton(
                    onClick = {
                        email = emailSuggestion
                        confirmedEmail = ""
                    }
                ) {
                    Text(
                        text = "Did you mean $emailSuggestion?",
                        fontSize = 13.sp,
                        color = SUGreen
                    )
                }

                TextButton(
                    onClick = {
                        confirmedEmail = normalizedEmail
                    }
                ) {
                    Text(
                        text = "No, my email is correct",
                        fontSize = 12.sp,
                        color = SUGray
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Password
            SignUpLabel("Password")

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Create a password",
                        fontSize = 14.sp,
                        color = SUGray
                    )
                },
                singleLine = true,
                isError = passwordError,
                visualTransformation =
                    if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (passwordVisible) {
                                    Icons.Outlined.Visibility
                                } else {
                                    Icons.Outlined.VisibilityOff
                                },
                            contentDescription =
                                "Toggle password visibility",
                            tint = SUGray,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = signUpFieldColors()
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "At least 8 characters, including a number.",
                fontSize = 12.sp,
                color = if (passwordError) {
                    MaterialTheme.colorScheme.error
                } else {
                    SUGray
                }
            )

            // Password validation feedback
            if (passwordValid) {
                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            SUSuccessBackground,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =
                            Icons.Outlined.CheckCircleOutline,
                        contentDescription = "Valid password",
                        tint = SUGreen,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "Your password meets the requirements.",
                        fontSize = 12.sp,
                        color = SUGray
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Terms checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = {
                        acceptedTerms = it
                    },
                    modifier = Modifier.size(22.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = SUGreen
                    )
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "I agree to the Terms of Use and Privacy Policy.",
                    fontSize = 12.sp,
                    color = SUDark
                )
            }

            if (termsError) {
                SignUpError(
                    "Please accept the Terms of Use and Privacy Policy"
                )
            }

            Spacer(Modifier.height(16.dp))

            // Optional email updates
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = emailOptIn,
                    onCheckedChange = {
                        emailOptIn = it
                    },
                    modifier = Modifier.size(22.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = SUGreen
                    )
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Send me local inspiration by email.",
                    fontSize = 12.sp,
                    color = SUDark
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        // Bottom actions
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    attempted = true

                    if (formValid) {
                        onSignUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(11.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SUGreen
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Continue",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 12.sp,
                    color = SUDark
                )

                Text(
                    text = "Log in",
                    modifier = Modifier.clickable {
                        onLogin()
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = SUDark
                )
            }

            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun SignUpLabel(label: String) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = SUDark
    )

    Spacer(Modifier.height(9.dp))
}

@Composable
private fun SignUpError(message: String) {
    Text(
        text = message,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(
            start = 4.dp,
            top = 5.dp
        )
    )
}

@Composable
private fun signUpFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color.White,
        focusedBorderColor = SUGreen,
        unfocusedBorderColor = SUBorder,
        cursorColor = SUGreen
    )
