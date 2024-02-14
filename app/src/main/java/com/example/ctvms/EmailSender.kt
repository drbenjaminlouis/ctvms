
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender {
    private val smtpHost: String = "smtp.gmail.com"
    private val smtpPort: String = "587"
    private val username: String = "ctvmsapp@gmail.com"
    private val password: String = "hvjddmgzwstamonh"

    private val messageHtml = """
        <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CTVMS Email</title>
</head>
<body style="margin: 0; padding: 20px; font-family: Arial, sans-serif; background-color: #141713; border-radius: 15px; color: white;">
    <div class='container' style="border: 5px solid #10af04; border-radius: 10px; max-width: 700px; margin: auto; padding: 20px;">
        <div class='header' style="text-align: center;">
            <img src='https://i.ibb.co/mF8vm0g/app-logo-removebg-preview.png' alt='Logo' style="max-width: 180px; height: auto; display: block; margin: 0 auto; margin-bottom: 20px;">
            <h1 class='title' style="font-size: 36px; text-align: center; margin-bottom: 20px; color: #10af04;">WELCOME TO CTVMS</h1>
        </div>
        <div class='message' style="color: white;">
            <p><strong>Dear {cust_name},</strong></p>
            <p>Welcome To CTVMS. Your account has been successfully created.</p>
            <p>As part of the account setup process, we have generated a one-time password (OTP) for you.</p>
            <p>Your OTP is: <strong>{otp}</strong></p>
            <p>Please use this OTP to complete your account setup process.</p>
            <p>If you have any questions or need assistance, feel free to contact us.</p>
        </div>
        <a class='button' href='mailto:ctvmsapp@gmail.com' style="display: block; margin: 0 auto; margin-top: 50px; padding: 10px 20px; background-color: #4CAF50; color: #fff; border: none; border-radius: 5px; font-size: 16px; font-weight: bold; cursor: pointer; transition: all 0.3s ease; text-decoration: none; text-align: center; width: fit-content;">CONTACT US</a>
        <div class='footer' style="text-align: center; margin-top: 40px;">
            <p>**Please do not reply to this email as it is an auto-generated message**</p>
            <img src='https://content3.jdmagicbox.com/comp/lucknow/w4/0522px522.x522.180411092220.d3w4/catalogue/maurya-cable-network-lda-colony-lucknow-cable-tv-operators-44iv1qoz5l.jpg?clr=3e3328' alt='Footer Image' style="max-width: 100%; height: auto;">
        </div>
    </div>

    <!-- Inline styles for responsive adjustments -->
    <style>
        @media screen and (max-width: 768px) {
            body {
                padding: 0 !important;
            }
        }
    </style>
    </body>
    </html>
    """.trimIndent()
    private val forgotEmail = """
        <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CTVMS Email</title>
</head>
<body style="margin: 0; padding: 20px; font-family: Arial, sans-serif; background-color: #141713; border-radius: 15px; color: white;">
    <div class='container' style="border: 5px solid #10af04; border-radius: 10px; max-width: 700px; margin: auto; padding: 20px;">
        <div class='header' style="text-align: center;">
            <img src='https://i.ibb.co/mF8vm0g/app-logo-removebg-preview.png' alt='Logo' style="max-width: 180px; height: auto; display: block; margin: 0 auto; margin-bottom: 20px;">
            <h1 class='title' style="font-size: 36px; text-align: center; margin-bottom: 20px; color: #10af04;">WELCOME TO CTVMS</h1>
        </div>
        <div class='message' style="color: white;">
            <p><strong>Dear {cust_name},</strong></p>
            <p>We have received a request to reset your password for your CTVMS account.</p>
            <p>To reset your password, please use the following one-time password (OTP):</p>
            <p>Your OTP is: <strong>{otp}</strong></p>
            <p>If you did not request a password reset, please ignore this email.</p>
            <p>If you have any questions or need assistance, feel free to contact us.</p>
        </div>
        <a class='button' href='mailto:ctvmsapp@gmail.com' style="display: block; margin: 0 auto; margin-top: 50px; padding: 10px 20px; background-color: #4CAF50; color: #fff; border: none; border-radius: 5px; font-size: 16px; font-weight: bold; cursor: pointer; transition: all 0.3s ease; text-decoration: none; text-align: center; width: fit-content;">CONTACT US</a>
        <div class='footer' style="text-align: center; margin-top: 40px;">
            <p>**Please do not reply to this email as it is an auto-generated message**</p>
            <img src='https://content3.jdmagicbox.com/comp/lucknow/w4/0522px522.x522.180411092220.d3w4/catalogue/maurya-cable-network-lda-colony-lucknow-cable-tv-operators-44iv1qoz5l.jpg?clr=3e3328' alt='Footer Image' style="max-width: 100%; height: auto;">
        </div>
    </div>

    <!-- Inline styles for responsive adjustments -->
    <style>
        @media screen and (max-width: 768px) {
            body {
                padding: 0 !important;
            }
        }
    </style>
    </body>
    </html>
    """.trimIndent()
    suspend fun sendOTP(context: Context, email: String, adminName: String, otp: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Sender's email address
            val from = "ctvmspp@gmail.com"

            // Create properties for SMTP configuration
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = smtpHost
            props["mail.smtp.port"] = smtpPort

            // Create a session with authentication
            val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })

            try {
                // Create a MIME message
                val fromAddress = InternetAddress(username, "CTVMS APP")
                val message = MimeMessage(session)
                message.setFrom(fromAddress)
                message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
                message.subject = "OTP Verification"

                // HTML content for the email
                val formattedHtmlContent = messageHtml.replace("{cust_name}", adminName)
                    .replace("{otp}", otp)

                // Set HTML content in the message
                message.setContent(formattedHtmlContent, "text/html")

                // Send the message
                Transport.send(message)
                println("OTP sent successfully to $email")
                true // Return true indicating success
            } catch (e: MessagingException) {
                println("Failed to send OTP: ${e.message}")
                false // Return false indicating failure
            }
        }
    }
    suspend fun sendForgotOTP(context: Context, email: String, adminName: String, otp: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Sender's email address
            val from = "ctvmspp@gmail.com"

            // Create properties for SMTP configuration
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = smtpHost
            props["mail.smtp.port"] = smtpPort

            // Create a session with authentication
            val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })

            try {
                // Create a MIME message
                val fromAddress = InternetAddress(username, "CTVMS APP")
                val message = MimeMessage(session)
                message.setFrom(fromAddress)
                message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
                message.subject = "Reset Password"

                // HTML content for the email
                val formattedHtmlContent = forgotEmail.replace("{cust_name}", adminName)
                    .replace("{otp}", otp)

                // Set HTML content in the message
                message.setContent(formattedHtmlContent, "text/html")

                // Send the message
                Transport.send(message)
                println("OTP sent successfully to $email")
                true // Return true indicating success
            } catch (e: MessagingException) {
                println("Failed to send OTP: ${e.message}")
                false // Return false indicating failure
            }
        }
    }
}
