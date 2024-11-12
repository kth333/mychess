import { EmailAPI } from "./EmailAPI";

class EmailService {
    async sendContactUsEmail(email) {
        return EmailAPI.post("/email/feedback", email);
    }
}

export default new EmailService();