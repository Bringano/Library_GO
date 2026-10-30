package se.yrgo.libraryapp.controllers;

import java.util.List;
import javax.inject.Inject;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import se.yrgo.libraryapp.dao.ClassificationDao;
import se.yrgo.libraryapp.entities.DdsClassification;

@Path("/classifications")
public class ClassificationController {
    private ClassificationDao classDao;

    @Inject
    ClassificationController(ClassificationDao classDao) {
        this.classDao = classDao;
    }

    @GET
    public List<DdsClassification> getAvailable() {
        return classDao.getAvailable();
    }
}
