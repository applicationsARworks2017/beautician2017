package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 1/1/18.
 */

public class Transactions {
    String id,debit,credit,balance,remarks,created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Transactions(String id, String debit, String credit, String balance, String remarks, String created) {
        this.id=id;
        this.debit=debit;
        this.credit=credit;
        this.balance=balance;
        this.remarks=remarks;
        this.created=created;

    }
}
