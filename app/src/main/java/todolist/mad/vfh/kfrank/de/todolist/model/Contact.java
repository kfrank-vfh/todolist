package todolist.mad.vfh.kfrank.de.todolist.model;

/**
 * Created by Kevin Frank on 13.06.2017.
 */

public class Contact {

    private long id;

    private String name;

    private String phone;

    private String mail;

    private byte[] photo;

    public Contact() {
    }

    public Contact(long id, String name, String phone, String mail, byte[] photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contact) {
            return id == ((Contact) obj).id;
        }
        return false;
    }
}
