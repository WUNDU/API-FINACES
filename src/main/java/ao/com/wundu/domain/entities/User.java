package ao.com.wundu.domain.entities;

import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.enums.PlanType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_preference", nullable = false)
    private NotificationPreference notificationPreference;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    @Column(name = "login_attempts")
    private int loginAttempts;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "data_consent")
    private boolean dataConsent; // Consentimento para Lei nº 22/11 e LGPD

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();

    // Construtor padrão
    public User() {
        this.planType = PlanType.FREE;
        this.loginAttempts = 0;
        this.locked = false;
        this.deleted = false;
        this.dataConsent = true;
        this.creditCards = new ArrayList<>();
    }

    // Construtor para criação de novo usuário
    public User(String name, String email, String password, String phone, NotificationPreference notificationPreference) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.notificationPreference = notificationPreference;
        this.planType = PlanType.FREE;
        this.loginAttempts = 0;
        this.locked = false;
        this.lockedUntil = null;
        this.deleted = false;
        this.dataConsent = true;
        this.creditCards = new ArrayList<>();
    }

    // Construtor completo (para recuperação do banco)
    public User(String id, String name, String email, String password, String phone,
                NotificationPreference notificationPreference, PlanType planType, int loginAttempts,
                boolean locked, LocalDateTime lockedUntil, boolean deleted, boolean dataConsent,
                LocalDateTime createdAt, LocalDateTime updatedAt, List<CreditCard> creditCards) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.notificationPreference = notificationPreference;
        this.planType = planType != null ? planType : PlanType.FREE;
        this.loginAttempts = loginAttempts;
        this.locked = locked;
        this.lockedUntil = lockedUntil;
        this.deleted = deleted;
        this.dataConsent = dataConsent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.creditCards = creditCards != null ? creditCards : new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public NotificationPreference getNotificationPreference() {
        return notificationPreference;
    }

    public void setNotificationPreference(NotificationPreference notificationPreference) {
        this.notificationPreference = notificationPreference;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public boolean isLocked() {
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDataConsent() {
        return dataConsent;
    }

    public void setDataConsent(boolean dataConsent) {
        this.dataConsent = dataConsent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
