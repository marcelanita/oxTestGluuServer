package org.testClient.controllers.v1;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testClient.model.TestModel;

@RestController
//@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/v1")
public class ApiController {



    @RequestMapping(value = "/test-get", method = RequestMethod.GET, produces = "application/json")
    public TestModel get() {
        TestModel testModel = new TestModel();
        testModel.setFeature("feat");
        return testModel;
    }


    @RequestMapping(value = "/test-post", method = RequestMethod.POST, produces = "application/json")
    public TestModel post() {
        TestModel testModel = new TestModel();
        testModel.setFeature("feat");
        return testModel;
    }
}
