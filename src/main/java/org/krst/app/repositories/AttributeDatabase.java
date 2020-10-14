package org.krst.app.repositories;

import org.krst.app.domains.Attribute;
import org.krst.app.models.Status;
import org.krst.app.utils.Constants;

import java.util.Optional;

public class AttributeDatabase extends Database<Attribute> {

    public AttributeDatabase() {
        databaseName = Constants.ATTRIBUTE_DATABASE;
    }

    @Override
    public Optional findById(Class c, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional findById(Class c, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status update(Attribute object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status delete(Attribute object) {
        throw new UnsupportedOperationException();
    }
}
