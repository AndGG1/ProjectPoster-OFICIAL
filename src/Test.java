import DTOS.EXTRA;
import DTOS.SignInView;

public class Test {

    public static void main(String[] args) {
        //This is temporary until we will have the Server and Client,
        // so that when the client joins, the data gets loaded and then the interface, not both in the same time.
        EXTRA.doIt("https://cdn-icons-png.flaticon.com/128/709/709724.png");
        EXTRA.doIt("https://cdn-icons-png.flaticon.com/128/9055/9055153.png");
        new SignInView();
    }
}