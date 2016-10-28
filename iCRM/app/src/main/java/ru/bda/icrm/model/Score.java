package ru.bda.icrm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Den Belobaba on 19.10.2016.
 * Данный класс Счет является счётом со всеми возможными параметрами
 */

public class Score implements Serializable {

    private String numberAccount = "";//номер счета
    private String client = "";//клиент
    private long dateAccount = 0;//дата счета
    private int sumScore = 0;//сумма счета
    private String contactFace = "";//контактное лицо
    private String contract = "";//договор
    private String responsible = "";//ответственное лицо
    private String initialConditions = "";//исходные условия
    private String orderWorks = "";//порядок выполнения работ
    private String annotation = "";//примечание
    private String linkVideo = "";//линка видео
    private String linkPhoto = "";//линка фото
    private String linkSound = "";//линка звука
    private int priority = 1;//приоритет
    private int status = 1;//статус
    private List<PriceSum> productList = new ArrayList<>();//товар в счете
    private boolean isInWork = true;//в работе или на согласовании

    public Score() {}

    /**
     * @return numberAccount возвращает номер счёта
     */
    public String getNumberAccount() {
        return numberAccount;
    }

    /**
     *
     * @param numberAccount номер счёта
     */
    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    /**
     *
     * @return возвращает клиента
     */
    public String getClient() {
        return client;
    }

    /**
     *
     * @param client присваивает клиента
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     *
     * @return возвращает дату счёта
     */
    public long getDateAccount() {
        return dateAccount;
    }

    public void setDateAccount(long dateAccount) {
        this.dateAccount = dateAccount;
    }

    public int getSumScore() {
        return sumScore;
    }

    public void setSumScore(int sumScore) {
        this.sumScore = sumScore;
    }

    public String getContactFace() {
        return contactFace;
    }

    public void setContactFace(String contactFace) {
        this.contactFace = contactFace;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getInitialConditions() {
        return initialConditions;
    }

    public void setInitialConditions(String initialConditions) {
        this.initialConditions = initialConditions;
    }

    public String getOrderWorks() {
        return orderWorks;
    }

    public void setOrderWorks(String orderWorks) {
        this.orderWorks = orderWorks;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }

    public String getLinkSound() {
        return linkSound;
    }

    public void setLinkSound(String linkSound) {
        this.linkSound = linkSound;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PriceSum> getProductList() {
        return productList;
    }

    public void setProductList(List<PriceSum> productList) {
        this.productList = productList;
    }

    public boolean isInWork() {
        return isInWork;
    }

    public void setInWork(boolean inWork) {
        isInWork = inWork;
    }

    @Override
    public String toString() {
        return "Score{" +
                "numberAccount='" + numberAccount + '\'' +
                ", client='" + client + '\'' +
                ", dateAccount=" + dateAccount +
                ", sumScore=" + sumScore +
                ", contactFace='" + contactFace + '\'' +
                ", contract='" + contract + '\'' +
                ", responsible='" + responsible + '\'' +
                ", initialConditions='" + initialConditions + '\'' +
                ", orderWorks='" + orderWorks + '\'' +
                ", annotation='" + annotation + '\'' +
                ", linkVideo='" + linkVideo + '\'' +
                ", linkPhoto='" + linkPhoto + '\'' +
                ", linkSound='" + linkSound + '\'' +
                '}';
    }
}
