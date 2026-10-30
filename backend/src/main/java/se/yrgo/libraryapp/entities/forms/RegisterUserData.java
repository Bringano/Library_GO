package se.yrgo.libraryapp.entities.forms;

public class RegisterUserData {
    private String name;
    private String realName;
    private String password;

    public RegisterUserData(String name, String realName, String password) {
        this.name = name;
        this.realName = realName;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((realName == null) ? 0 : realName.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegisterUserData other = (RegisterUserData) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (realName == null) {
            if (other.realName != null)
                return false;
        }
        else if (!realName.equals(other.realName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        }
        else if (!password.equals(other.password))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RegisterUserData [name=" + name + ", realName=" + realName + ", password=" + password + "]";
    }
}
