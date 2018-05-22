package com.udacity.booklisting.models;


public class Book {

    /* Attributes of Book Object */

    /* The of Book to be provides from API request */
    private String mId;
    /* Name of the Book */
    private String mTitle;
    /* Authors of the Book */
    private String authors;
    /* Number of pages of the Book */
    private int mPageCount;
    /* Average users rating of the Book */
    private float mAverageRating;
    /* The price of the Book - Might be 0 if the book is free*/
    private double mListPrice;
    /* Is the currency of the price in three letters (ie "BLR" or "USD") */
    private String mCurrencyCode;

    /* The sub-title of the Book */
    private String mSubTitle;
    /* The publisher of the Book. Usually It is a company*/
    private String mPublisher;
    /* Date when the Book was published - Comes from API as String */
    private String mPublishedDate;
    /* A short description of the Book - May be empty*/
    private String mDescription;
    /* The URL that leads to a Play Book website page where the Book can be bought. */
    private String mbuyLink;
    /* Number of ratings given by users to a Book - might be 0 */
    private int mRatingsCount;
    /* The link of the Book cover image */
    private String mImageLink;

    /**
     * Creates a empty Book Object.
     */
    public Book() {
    }

    /**
     * Creates a Book Object to be used in the BookList
     * @param mId is the Id of the book.
     * @param mTitle is the Title of the book.
     * @param authors are the Authors of the book. Might be one or more.
     * @param mPageCount is the number of pages of the book.
     * @param mAverageRating is the average rating given by users of the book.
     * @param mListPrice is the list price of the book.
     * @param mCurrencyCode is the currency os the price of the book.
     */
    public Book(String mId, String mTitle, String authors, int mPageCount, float mAverageRating, double mListPrice, String mCurrencyCode) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.authors = authors;
        this.mPageCount = mPageCount;
        this.mAverageRating = mAverageRating;
        this.mListPrice = mListPrice;
        this.mCurrencyCode = mCurrencyCode;
    }

    /**
     *If the constructor to be used in the BookDetailsActivity.
     * @param mId is the Id of the book.
     * @param mTitle is the Title of the book.
     * @param authors are the Authors of the book. Might be one or more.
     * @param mPageCount is the number of pages of the book.
     * @param mAverageRating is the average rating given by users of the book.
     * @param mListPrice is the list price of the book.
     * @param mCurrencyCode is the currency os the price of the book.
     * @param mSubTitle is the subtitle of the Book.
     * @param mPublisher  is the Publisher of the Book.
     * @param mPublishedDate is the Date when the Book was published in a String data type.
     * @param mDescription is a short description of the book.
     * @param mbuyLink the Google Play Book link to buy the book.
     * @param mRatingsCount is the total number os ratings given by users.
     * @param mImageLink is the link of the Book cover image.
     */
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

    /**
     * Methods Getters and Setters to be used in any instance of Book object class.
     */
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

