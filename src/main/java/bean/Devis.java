package bean;

import java.time.LocalDate;

public class Devis {
    private Long id;
    private double estimatedAmount;
    private LocalDate issueDate;
    private LocalDate validatedDate;
    private boolean isAccepted;
    private Projet projet;
    public Devis() {}

    public Devis(Long id, double estimatedAmount, LocalDate issueDate, LocalDate validatedDate, boolean isAccepted, Projet projet) {
        this.id = id;
        this.estimatedAmount = estimatedAmount;
        this.issueDate = issueDate;
        this.validatedDate = validatedDate;
        this.isAccepted = isAccepted;
        this.projet = projet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(LocalDate validatedDate) {
        this.validatedDate = validatedDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    @Override
    public String toString() {
        return "Devis{" +
                "id=" + id +
                ", estimatedAmount=" + estimatedAmount +
                ", issueDate=" + issueDate +
                ", validatedDate=" + validatedDate +
                ", isAccepted=" + isAccepted +
                ", projet=" + projet +
                '}';
    }


}
