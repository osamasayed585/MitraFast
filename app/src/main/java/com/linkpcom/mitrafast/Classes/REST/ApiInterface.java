package com.linkpcom.mitrafast.Classes.REST;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritesShops;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AcceptAgentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddBankAccountRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddFavoritePlacesRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentCardRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AllShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CancelOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChangePaymentTypeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeBalanceRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CompleteDataRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ContactUsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditAgentProfileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditBankAccountRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditClientProfileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ForgetPasswordRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.LoginRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.MakeOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.MessageNotificationRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ProductDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ProductsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RateAgentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RateClientRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RejectAgentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SearchShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SendOfferRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SuggestRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.UpdateOrderStatusRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AboutUsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AdsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AgentDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AgentProfileResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AuthResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BankAccountDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BankAccountResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BanksResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CancelOrderReasonsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CategoriesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ClientProfileResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CompaniesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CompanyProfileResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CouponResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CouponsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FAQResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FavoritePlacesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FavoriteResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FinancialMovementsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ForgetPasswordResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.IntroSlidersResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.LoginResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NationalitiesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NotificationsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OffersResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentMethodsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.RegisterResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ReplyResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.StatusesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.SubjectsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.SupportResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketTypesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TransactionsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ValueAddedTaxResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("openScreen")
    Observable<IntroSlidersResponse> requestSliders();

    @POST("categories")
    Observable<CategoriesResponse> requestCategories();

    @POST("slider-user")
    Observable<AdsResponse> requestAds();

    @FormUrlEncoded
    @POST("user/slider-click")
    Observable<BaseResponse> requestAdClick(@Field("slider_id") int slider_id);

    @FormUrlEncoded
    @POST("categories")
    Observable<CategoriesResponse> requestCategoriesSearch(@Field("keyword") String keyword);


    @POST("shops")
    Observable<ShopsResponse> requestShops(@Body ShopsRequest request);

    @POST("user/favourite-shops")
    Observable<FavoriteResponse> requestFavorites(@Body ShopsRequest request);


    @POST("user/add-favourite-shop")
    Observable<BaseResponse> requestAddFavorite(@Query("shop_id") int shop_id);

    @POST("user/remove-favourite-shop")
    Observable<BaseResponse> requestRemoveFavorite(@Query("shop_id") int shop_id);

    @POST("all-shops")
    Observable<ShopsResponse> requestAllShops(@Body AllShopsRequest request);

    @POST("search-shops")
    Observable<ShopsResponse> requestSearchShops(@Body SearchShopsRequest request);


    @POST("shop/products")
    Observable<ProductsResponse> requestProducts(@Body ProductsRequest request);

    @FormUrlEncoded
    @POST("shop/categories")
    Observable<CategoriesResponse> requestShopCategories(@Field("shop_id") int shop_id);


    @POST("shop-details")
    Observable<ShopDetailsResponse> requestShopDetails(@Body ShopDetailsRequest request);


    @FormUrlEncoded
    @POST("classifications")
    Observable<CategoriesResponse> requestSubCategories(@Field("category_id") int category_id);


    @POST("user/profile")
    Observable<ClientProfileResponse> requestClientProfile();

    @POST("user/updateProfile")
    Observable<ClientProfileResponse> requestEditClientProfile(@Body EditClientProfileRequest request);

    @Multipart
    @POST("user/updateProfile")
    Observable<ClientProfileResponse> requestEditClientProfile(
            @Query("name") String name,
            @Query("address") String address,
            @Part MultipartBody.Part image
    );

    @POST("provider/profile")
    Observable<AgentProfileResponse> requestAgentProfile();

    @POST("provider/currentOrders")
    Observable<OrdersResponse> requestAgentCurrentOrders();

    @POST("user/currentOrders")
    Observable<OrdersResponse> requestUserCurrentOrders();

    @POST("user/current-orders")
    Observable<OrdersResponse> getCurrentOrders();


    @POST("provider/updateProfile")
    Observable<AgentProfileResponse> requestEditAgentProfile(@Body EditAgentProfileRequest request);

    @Multipart
    @POST("provider/updateProfile")
    Observable<AgentProfileResponse> requestEditAgentProfile(
            @Query("visible_name") String visible_name,
            @Query("real_name") String real_name,
            @Query("identity_id") String identity_id,
            @Query("email") String email,
            @Part MultipartBody.Part image
    );


    @POST("user/orders")
    Observable<OrdersResponse> requestOrders();

    @POST("user/user-wallet-total-money")
    Observable<WalletResponse> requestWalletBalance();

    @POST("provider/finishOrders")
    Observable<OrdersResponse> requestFinishedOrders();


    @POST("aboutUs")
    Observable<AboutUsResponse> requestAboutUs();

    @POST("contact-us")
    Observable<BaseResponse> requestContactUs(@Body ContactUsRequest request);


    @POST("frequently-asked-questions")
    Observable<FAQResponse> requestFAQ();


    @FormUrlEncoded
    @POST("frequently-asked-questions")
    Observable<FAQResponse> requestFAQ(@Field("support_section_id") int support_section_id);


    @Multipart
    @POST("shop/register")
    Observable<BaseResponse> requestShopRegister(
            @Query("name_ar") String name_ar,
            @Query("name_en") String name_en,
            @Query("commercial_register") String commercial_register,
            @Query("owner_mobile") String owner_mobile,
            @Query("mobile") String mobile,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("address") String address,
            @Query("work_start_at") String work_start_at,
            @Query("end_start_at") String end_start_at,
            @Query("end_start_at") long value_added_number,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part commercial_registe_image,
            @Part MultipartBody.Part owner_identity_image
    );


    @POST("user/new-register")
    Observable<LoginResponse> requestLogin(@Body LoginRequest loginRequest);

    @POST("provider/login")
    Observable<AuthResponse> requestAgentLogin(@Body LoginRequest loginRequest);

    @POST("user/confirmProfileByFirebase")
    Observable<ClientProfileResponse> requestActivateAccount();


    @Multipart
    @POST("user/updateProfile")
    Observable<RegisterResponse> requestCompleteData(
            @Query("country_id") String country_id,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("password") String password,
            @Query("fire_base_token") String fire_base_token,

            @Part MultipartBody.Part image
    );


    @POST("user/updateProfile")
    Observable<RegisterResponse> requestCompleteData(@Body CompleteDataRequest loginRequest);


    @Multipart
    @POST("provider/register")
    Observable<RegisterResponse> requestAgentRegister(
            @Query("mobile") String mobile,
            @Query("country_id") int country_id,
            @Query("name") String name,
            @Query("identity_name") String identity_name,
            @Query("identity_id") String identity_id,
            @Query("email") String email,
            @Query("birthday_date") String birthday_date,
            @Query("nationality_id") int nationality_id,
            @Query("company_id") String company_id,
            @Query("time_type") String time_type,
            @Query("stc_pay_number") String stc_pay_number,
            @Query("fire_base_token") String fire_base_token,

            @Part MultipartBody.Part image,
            @Part MultipartBody.Part identity_image,
            @Part MultipartBody.Part driving_license_image,
            @Part MultipartBody.Part car_insurance_image

    );


    @POST("banks")
    Observable<BanksResponse> requestBanks();

    @POST("nationalities")
    Observable<NationalitiesResponse> requestNationalities();

    @POST("bankAccounts")
    Observable<BankAccountResponse> requestBankAccountDetails();

    @POST("provider/edit-bank-accounts")
    Observable<BaseResponse> requestEditBankAccount(@Body EditBankAccountRequest request);

    @POST("provider/add-bank-accounts")
    Observable<BaseResponse> requestAddBankAccount(@Body AddBankAccountRequest request);


    @POST("termsAndConditions")
    Observable<TermsAndConditionsResponse> requestTermsAndConditions();

    @POST("agentTickets")
    Observable<TicketsResponse> requestTickets();

    @FormUrlEncoded
    @POST("deleteTicket")
    Observable<BaseResponse> requestDeleteTicket(@Field("ticket_id") int ticket_id);


    @Multipart
    @POST("storeTicket")
    Observable<BaseResponse> requestAgentAddTicket(
            @Query("ticket_type_id") int ticket_type_id,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("message") String message,
            @Part MultipartBody.Part... images
    );

    @POST("storeTicket")
    Observable<BaseResponse> requestAgentAddTicketWithoutImages(
            @Query("ticket_type_id") int ticket_type_id,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("message") String message
    );

    @Multipart
    @POST("user/add-ticket-support-sections")
    Observable<BaseResponse> requestClientAddTicket(
            @Query("support_section_id") int support_section_id,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("email") String email,
            @Query("message") String message,
            @Part MultipartBody.Part... images
    );

    @POST("user/add-ticket-support-sections")
    Observable<BaseResponse> requestClientAddTicketWithoutImages(
            @Query("support_section_id") int support_section_id,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("email") String email,
            @Query("message") String message
    );

    @POST("ticketTypes")
    Observable<TicketTypesResponse> requestTicketTypes();

    @POST("user-notifications")
    Observable<NotificationsResponse> requestNotifications();


    @POST("user-seen-notifications")
    Observable<BaseResponse> requestSeenNotifications();


    @FormUrlEncoded
    @POST("orderDetails")
    Observable<OrderDetailsResponse> requestOrderDetails(@Field("order_id") int order_id);

    @POST("provider/acceptOrder")
    Observable<BaseResponse> requestSendOffer(@Body SendOfferRequest request);

    @FormUrlEncoded
    @POST("provider/orderFinishStatus")
    Observable<BaseResponse> requestFinishOrder(@Field("order_id") int order_id);

    @POST("provider/cancelOrderReasons")
    Observable<CancelOrderReasonsResponse> requestAgentCancelOrderReasons();


    @POST("provider/cancelOrder")
    Observable<BaseResponse> requestAgentCancelOrder(@Body CancelOrderRequest cancelOrderRequest);

    @POST("provider/rateOrder")
    Observable<BaseResponse> requestRateClient(@Body RateClientRequest request);

    @POST("user/rateOrder")
    Observable<BaseResponse> requestRateAgent(@Body RateAgentRequest request);


    @POST("shop/productDetails")
    Observable<ProductDetailsResponse> requestProductDetails(@Body ProductDetailsRequest request);

    @POST("user/addOrder")
    Observable<OrderDetailsResponse> requestMakeOrder(@Body MakeOrderRequest request);

    @POST("tax")
    Observable<ValueAddedTaxResponse> requestValueAddedTax();

    @FormUrlEncoded
    @POST("user/request-add-promo-code")
    Observable<CouponResponse> requestCoupon(@Field("code") String code);

    @FormUrlEncoded
    @POST("favourite-places")
    Observable<FavoritePlacesResponse> requestFavoritePlaces(@Field("page") int page);

    @FormUrlEncoded
    @POST("remove-favourite-place")
    Observable<BaseResponse> requestRemoveFavoritePlace(@Field("favourite_place_id") int favourite_place_id);

    @POST("add-favourite-place")
    Observable<BaseResponse> requestAddAddress(@Body AddFavoritePlacesRequest request);


    @POST("paymentTypes")
    Observable<PaymentMethodsResponse> requestPaymentMethods();

    @POST("user/cancelOrderReasons")
    Observable<CancelOrderReasonsResponse> requestClientCancelOrderReasons();


    @POST("user/cancelOrder")
    Observable<BaseResponse> requestUserCancelOrder(@Body CancelOrderRequest cancelOrderRequest);


    @FormUrlEncoded
    @POST("provider/accountDetails")
    Observable<AgentDetailsResponse> requestAgentDetails(@Field("provider_id") String provider_id);

    @POST("user/userAcceptOrderDriver")
    Observable<BaseResponse> requestAcceptsAgent(@Body AcceptAgentRequest request);

    @POST("user/userRejectOrderDriver")
    Observable<BaseResponse> requestRejectAgent(@Body RejectAgentRequest request);

    @FormUrlEncoded
    @POST("provider/rejectOrder")
    Observable<BaseResponse> requestIgnoreOrder(@Field("order_id") int order_id);


    @POST("forgetPassword/reset")
    Observable<ForgetPasswordResponse> requestForgetPassword(@Body ForgetPasswordRequest request);

    @FormUrlEncoded
    @POST("forgetPassword/changeForgottenPassword")
    Observable<BaseResponse> requestChangeForgottenPassword(@Field("password") String password);


    @FormUrlEncoded
    @POST("provider/orderStatus")
    Observable<StatusesResponse> requestOrderStatuses(@Field("order_id") int order_id);


    @POST("provider/orderUpdateStatus")
    Observable<BaseResponse> requestUpdateOrderStatus(@Body UpdateOrderStatusRequest request);


    @POST("provider/financialMovement")
    Observable<FinancialMovementsResponse> requestFinancialMovements();

    @POST("add-payment")
    Observable<PaymentResponse> requestPayment(@Body PaymentRequest addTicketRequest);


    @POST("user/updateOrderPaymentMethod")
    Observable<BaseResponse> requestChangePaymentType(@Body ChangePaymentTypeRequest request);

    @POST("offers")
    Observable<OffersResponse> requestOffers();

    @FormUrlEncoded
    @POST("terms-and-conditions")
    Observable<TermsAndConditionsResponse> requestTerms(@Field("user_type_id") int user_type_id);

    @POST("user/user-wallet-transactions")
    Observable<TransactionsResponse> requestTransactions();

    @POST("user/user-wallet-total-money")
    Observable<WalletResponse> requestWallet();


    @POST("user/list-credit-cards")
    Observable<PaymentCardsResponse> requestPaymentCards();

    @POST("user/remove-credit-cards")
    Observable<BaseResponse> requestDeletePaymentCard(@Query("credit_card_id") int paymentId);

    @POST("user/add-credit-card")
    Observable<PaymentCardResponse> requestAddPaymentCard(@Body AddPaymentCardRequest request);


    @POST("user/list-promo-code")
    Observable<CouponsResponse> requestCoupons();

    @FormUrlEncoded
    @POST("user/add-promo-code")
    Observable<BaseResponse> requestAddCoupon(@Field("code") String code);

    @POST("......")
    Observable<OrdersResponse> requestBills();

    @POST("contact-us-subjects")
    Observable<SubjectsResponse> requestSubjects();


    @POST("support-sections")
    Observable<SupportResponse> requestSupport();


    @POST("add-suggestion")
    Observable<BaseResponse> requestSuggest(@Body SuggestRequest request);

    @POST("user/list-support-tickets")
    Observable<TicketsResponse> requestClientTickets();

    @POST("user/reply-list-for-support-ticket")
    Observable<ReplyResponse> requestClientReply(@Query("support_ticket_id") int supportTicketId);


    @Multipart
    @POST("user/add-reply-support-tickets")
    Observable<BaseResponse> requestAddTicketReply(
            @Query("support_ticket_id") int support_ticket_id,
            @Query("reply") String reply,
            @Part MultipartBody.Part... images
    );

    @POST("user/add-reply-support-tickets")
    Observable<BaseResponse> requestAddTicketReplyWithoutImages(
            @Query("support_ticket_id") int support_ticket_id,
            @Query("reply") String reply
    );

    @POST("user/add-user-wallet-transaction")
    Observable<BaseResponse> requestCharge(@Body ChargeRequest request);

    @POST("charge-wallet-payment")
    Observable<PaymentResponse> requestChargeBalance(@Body ChargeBalanceRequest addTicketRequest);

    @Multipart
    @POST("user/update-profile-image")
    Observable<AuthResponse> requestUpdateImage(@Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("provider/provider-accept-order")
    Observable<BaseResponse> requestAgentAcceptOrder(@Field("order_id") int order_id);

    @POST("user/send-new-message-notification")
    Observable<BaseResponse> requestMessageNotification(@Body MessageNotificationRequest request);

    @Multipart
    @POST("user/send-file-message")
    Observable<BaseResponse> requestSendChatFile(
            @Query("order_id") String chat_id,
            @Query("file_type") String file_type,
            @Query("reply_key") String reply_key,
            @Part MultipartBody.Part file
    );

    @POST("companies")
    Observable<CompaniesResponse> requestCompanies();

    @POST("user/company_profile")
    public Observable<CompanyProfileResponse> requestCompanyProfile();

    @POST("check-creditcard-validation")
    Observable<PaymentResponse> requestValidateCard(@Body AddPaymentRequest paymentRequest);

    @FormUrlEncoded
    @POST("user/update-credit-card-status")
    Observable<BaseResponse> requestCardValid(@Field("credit_card_id") int credit_card_id);
}
