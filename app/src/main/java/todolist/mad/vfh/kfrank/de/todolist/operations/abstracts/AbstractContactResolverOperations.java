package todolist.mad.vfh.kfrank.de.todolist.operations.abstracts;

import todolist.mad.vfh.kfrank.de.todolist.model.Contact;
import todolist.mad.vfh.kfrank.de.todolist.operations.interfaces.IContactAccessOperations;

/**
 * Created by Kevin Frank on 14.06.2017.
 */

public abstract class AbstractContactResolverOperations {

    private IContactAccessOperations contactAccessOperations;

    public AbstractContactResolverOperations(IContactAccessOperations contactAccessOperations) {
        this.contactAccessOperations = contactAccessOperations;
    }

    protected Contact getContactToId(String id) {
        return contactAccessOperations.getContactToId(id);
    }
}
