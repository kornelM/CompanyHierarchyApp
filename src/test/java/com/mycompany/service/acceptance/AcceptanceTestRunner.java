package com.mycompany.service.acceptance;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features={"src/test/resources"}
)
public class AcceptanceTestRunner {
    //If you see this you are probably a person who checks this project.
    //There is no acceptance test - I know its huge defect but to be honest I do not know how to write them yet.
    //I will try to change it very soon.
}
