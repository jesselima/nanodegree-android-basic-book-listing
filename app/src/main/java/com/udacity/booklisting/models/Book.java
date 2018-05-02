package com.udacity.booklisting.models;


public class Book {

    private String mId;
    private String mTitle;
    private String authors;
    private int mPageCount;
    private float mAverageRating;
    private double mListPrice;
    private String mCurrencyCode;

    private String mSubTitle;
    private String mPublisher;
    private String mPublishedDate;
    private String mDescription;
    private String mbuyLink;
    private int mRatingsCount;
    private String mImageLink;

    public Book() {
    }


    public Book(String mId, String mTitle, String authors, int mPageCount, float mAverageRating, double mListPrice, String mCurrencyCode) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.authors = authors;
        this.mPageCount = mPageCount;
        this.mAverageRating = mAverageRating;
        this.mListPrice = mListPrice;
        this.mCurrencyCode = mCurrencyCode;
    }

    public Book(String mId, String mTitle, String authors, int mPageCount, float mAverageRating, double mListPrice, String mCurrencyCode, String mSubTitle, String mPublisher, String mPublishedDate, String mDescription, String mbuyLink, int mRatingsCount, String mImageLink) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.authors = authors;
        this.mPageCount = mPageCount;
        this.mAverageRating = mAverageRating;
        this.mListPrice = mListPrice;
        this.mCurrencyCode = mCurrencyCode;
        this.mSubTitle = mSubTitle;
        this.mPublisher = mPublisher;
        this.mPublishedDate = mPublishedDate;
        this.mDescription = mDescription;
        this.mbuyLink = mbuyLink;
        this.mRatingsCount = mRatingsCount;
        this.mImageLink = mImageLink;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int mPageCount) {
        this.mPageCount = mPageCount;
    }

    public float getAverageRating() {
        return mAverageRating;
    }

    public void setAverageRating(float mAverageRating) {
        this.mAverageRating = mAverageRating;
    }

    public double getListPrice() {
        return mListPrice;
    }

    public void setListPrice(double mListPrice) {
        this.mListPrice = mListPrice;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    public void setCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }

    public String getmSubTitle() {
        return mSubTitle;
    }

    public void setmSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public void setmPublishedDate(String mPublishedDate) {
        this.mPublishedDate = mPublishedDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getMbuyLink() {
        return mbuyLink;
    }

    public void setMbuyLink(String mbuyLink) {
        this.mbuyLink = mbuyLink;
    }

    public int getmRatingsCount() {
        return mRatingsCount;
    }

    public void setmRatingsCount(int mRatingsCount) {
        this.mRatingsCount = mRatingsCount;
    }

    public void setmImageLink(String mImageLink) {
        this.mImageLink = mImageLink;
    }

    public String getmImageLink() {
        return mImageLink;
    }
}

