package DTOS.UserInterfaces.Register;

public final class Consistency {
    
    boolean isSign = true;
    boolean isLog = false;
    
    SignInView signIn;
    LogInView logIn;
    
    String img = "";
    
    public boolean isSign() {
        return isSign;
    }
    
    public void setSign(boolean sign) {
        isSign = sign;
    }
    
    public boolean isLog() {
        return isLog;
    }
    
    public void setLog(boolean log) {
        isLog = log;
    }
    
    public SignInView getSignIn() {
        return signIn;
    }
    
    public void setSignIn(SignInView signIn) {
        this.signIn = signIn;
    }
    
    public LogInView getLogIn() {
        return logIn;
    }
    
    public void setLogIn(LogInView logIn) {
        this.logIn = logIn;
    }
    
    public String getImg() {
        return img;
    }
    
    public void setImg(String img) {
        this.img = img;
    }
}
