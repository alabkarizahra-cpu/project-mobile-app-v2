
package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LLBackground = Color(0xFFFAF8F3)
private val LLGreen = Color(0xFF176B52)
private val LLDark = Color(0xFF24303D)
private val LLGray = Color(0xFF718096)
private val LLBorder = Color(0xFFE7E5DF)
private val LLIconBackground = Color(0xFFE8F2EC)

@Composable
fun LoginScreen(
    onLogin: () -> Unit = {},
    onSignUp: () -> Unit = {},
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var attempted by remember { mutableStateOf(false) }
    var confirmedEmail by remember { mutableStateOf("") }

    val normalizedEmail = email.trim()

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

    val emailError = attempted &&
            (!emailValid || needsEmailConfirmation)

    val passwordError = attempted && password.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LLBackground)
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {

        // Scrollable upper section
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // App bar
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
                        tint = LLGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Welcome back",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LLDark
                )
            }

            Spacer(Modifier.height(36.dp))

            // Location icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = LLIconBackground,
                        shape = RoundedCornerShape(17.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(29.dp),
                    tint = LLGreen
                )
            }

            Spacer(Modifier.height(24.dp))

            // Main title
            Text(
                text = "Your next discovery\nawaits.",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp,
                color = LLDark
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Log in to save your favorite places and pick up where you left off.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = LLGray
            )

            Spacer(Modifier.height(26.dp))

            // Email
            Text(
                text = "Email address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LLDark
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    confirmedEmail = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                placeholder = {
                    Text(
                        text = "noura@example.com",
                        fontSize = 14.sp,
                        color = LLGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        modifier = Modifier.size(20.dp),
                        tint = LLGray
                    )
                },
                singleLine = true,
                isError = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White,
                    focusedBorderColor = LLGreen,
                    unfocusedBorderColor = LLBorder,
                    cursorColor = LLGreen
                )
            )

            if (attempted && !emailValid) {
                Text(
                    text = "Enter a valid email address",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        top = 5.dp
                    )
                )
            } else if (attempted && needsEmailConfirmation) {
                Text(
                    text = "Please review your email address",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        top = 5.dp
                    )
                )
            }

            if (needsEmailConfirmation &&
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
                        color = LLGreen,
                        fontSize = 13.sp
                    )
                }

                TextButton(
                    onClick = {
                        confirmedEmail = normalizedEmail
                    }
                ) {
                    Text(
                        text = "No, my email is correct",
                        color = LLGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Password
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LLDark
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                placeholder = {
                    Text(
                        text = "Enter your password",
                        color = LLGray,
                        fontSize = 14.sp
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
                            tint = LLGray,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White,
                    focusedBorderColor = LLGreen,
                    unfocusedBorderColor = LLBorder,
                    cursorColor = LLGreen
                )
            )

            if (passwordError) {
                Text(
                    text = "Password is required",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        top = 5.dp
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // Forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot password?",
                    modifier = Modifier.clickable {
                        onForgotPassword()
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LLGreen
                )
            }

            Spacer(Modifier.height(18.dp))

            // Keep me logged in
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = {
                        rememberMe = it
                    },
                    modifier = Modifier.size(22.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = LLGreen
                    )
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Keep me logged in",
                    fontSize = 13.sp,
                    color = LLDark
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "A little curiosity goes a long way.",
                fontSize = 12.sp,
                color = LLGray
            )

            Spacer(Modifier.height(20.dp))
        }

        // Bottom actions
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    attempted = true

                    if (
                        emailValid &&
                        !needsEmailConfirmation &&
                        password.isNotBlank()
                    ) {
                        onLogin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(11.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LLGreen
                )
            ) {
                Text(
                    text = "Log in",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New to LocalLens? ",
                    fontSize = 12.sp,
                    color = LLDark
                )

                Text(
                    text = "Create an account",
                    modifier = Modifier.clickable {
                        onSignUp()
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = LLDark
                )
            }

            Spacer(Modifier.height(22.dp))
        }
    }
}
