package Functional;

import org.testng.annotations.Test;

public class MainPageTests extends Functional.BaseTest {

    @Test(description = "Test sprawdza...")
    public void shouldOpenPage(){
        mainPage
                .launch();
    }
}
