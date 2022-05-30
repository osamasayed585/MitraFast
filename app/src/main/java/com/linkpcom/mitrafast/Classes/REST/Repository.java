package com.linkpcom.mitrafast.Classes.REST;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessaging;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AcceptAgentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddBankAccountRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddFavoritePlacesRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentCardRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketReplyRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AgentRegisterRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AllShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CancelOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChangePaymentTypeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeBalanceRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChatFileRequest;
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
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SameProductRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SearchShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SendOfferRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopRegisterRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SuggestRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.UpdateOrderStatusRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AboutUsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AddressResponse;
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
import com.linkpcom.mitrafast.Classes.RoomDB.db.AppDatabase;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import okhttp3.MultipartBody;
import timber.log.Timber;

@ViewModelScoped
public class Repository {
    private static String fireBaseToken;
    private final ApiInterface apiInterface;
    private final AppDatabase db;


    @Inject
    public Repository(ApiInterface apiInterface, AppDatabase db) {
        this.apiInterface = apiInterface;
        this.db = db;

        getToken();
    }

    private void getToken() {
        if (fireBaseToken == null) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Timber.w(task.getException(), "getInstanceId failed");
                            getToken();
                            return;
                        }
                        fireBaseToken = task.getResult();
                    });
        }
    }


    public Observable<IntroSlidersResponse> requestSliders() {
        return apiInterface.requestSliders();
    }

    public Observable<AdsResponse> requestAds() {
        return apiInterface.requestAds();
    }

    public Observable<BaseResponse> requestAdClick(int adId) {
        return apiInterface.requestAdClick(adId);
    }

    public Observable<CategoriesResponse> requestCategories() {
        return apiInterface.requestCategories();
    }

    public Observable<CategoriesResponse> requestCategoriesSearch(String keyword) {
        return apiInterface.requestCategoriesSearch(keyword);
    }


    public Observable<ShopsResponse> requestShops(ShopsRequest request) {
        return apiInterface.requestShops(request);
    }

    public Observable<FavoriteResponse> requestFavorites(ShopsRequest request) {
        return apiInterface.requestFavorites(request);
    }

    public Observable<BaseResponse> requestAddFavorite(int shop_id) {
        return apiInterface.requestAddFavorite(shop_id);
    }

    public Observable<BaseResponse> requestRemoveFavorite(int shop_id) {
        return apiInterface.requestRemoveFavorite(shop_id);
    }

    public Observable<ShopsResponse> requestAllShops(AllShopsRequest request) {
        return apiInterface.requestAllShops(request);
    }

    public Observable<ShopsResponse> requestSearchShops(SearchShopsRequest request) {
        return apiInterface.requestSearchShops(request);
    }


    public Observable<CategoriesResponse> requestSubCategories(int categoryId) {
        return apiInterface.requestSubCategories(categoryId);
    }

    public Observable<ProductsResponse> requestProducts(ProductsRequest request) {
        return apiInterface.requestProducts(request);
    }

    public Observable<CategoriesResponse> requestShopCategories(int shopId) {
        return apiInterface.requestShopCategories(shopId);
    }

    public Observable<ShopDetailsResponse> requestShopDetails(ShopDetailsRequest request) {
        return apiInterface.requestShopDetails(request);
    }

    public Observable<ClientProfileResponse> requestClientProfile() {
        return apiInterface.requestClientProfile();
    }


    public Observable<ClientProfileResponse> requestEditClientProfile(EditClientProfileRequest request) {
        if (request.getImage() != null)
            return apiInterface.requestEditClientProfile(
                    request.getName(),
                    request.getAddress(),
                    request.getImage());
        else
            return apiInterface.requestEditClientProfile(request);
    }

    public Observable<AgentProfileResponse> requestAgentProfile() {
        return apiInterface.requestAgentProfile();
    }

    public Observable<OrdersResponse> requestAgentCurrentOrders() {
        return apiInterface.requestAgentCurrentOrders();
    }

    public Observable<OrdersResponse> requestUserCurrentOrders() {
        return apiInterface.requestUserCurrentOrders();
    }

    public Observable<OrdersResponse> getCurrentOrders() {
        return apiInterface.getCurrentOrders();
    }

    public Observable<AgentProfileResponse> requestEditAgentProfile(EditAgentProfileRequest request) {
        if (request.getImage() != null)
            return apiInterface.requestEditAgentProfile(
                    request.getName(),
                    request.getIdentity_name(),
                    request.getIdentity_id(),
                    request.getEmail(),
                    request.getImage());
        else
            return apiInterface.requestEditAgentProfile(request);
    }


    public Observable<OrdersResponse> requestOrders() {
        return apiInterface.requestOrders();
    }

    public Observable<WalletResponse> requestWalletBalance() {
        return apiInterface.requestWalletBalance();
    }

    public Observable<OrdersResponse> requestFinishedOrders() {
        return apiInterface.requestFinishedOrders();
    }


    public Observable<AboutUsResponse> requestAboutUs() {
        return apiInterface.requestAboutUs();
    }

    public Observable<BaseResponse> requestContactUs(ContactUsRequest request) {
        return apiInterface.requestContactUs(request);
    }

    public Observable<FAQResponse> requestFAQ() {
        return apiInterface.requestFAQ();
    }

    public Observable<FAQResponse> requestFAQ(int supportSectionId) {
        return apiInterface.requestFAQ(supportSectionId);
    }

    public Observable<BaseResponse> requestShopRegister(ShopRegisterRequest request) {
        return apiInterface.requestShopRegister(
                request.getName_ar(),
                request.getName_en(),
                request.getCommercial_register(),
                request.getOwner_mobile(),
                request.getMobile(),
                request.getLat(),
                request.getLng(),
                request.getAddress(),
                request.getWork_start_at(),
                request.getEnd_start_at(),
                request.getValue_added_number(),
                request.getImage(),
                request.getCommercial_registe_image(),
                request.getOwner_identity_image()

        );
    }


    public Observable<AuthResponse> requestAgentLogin(LoginRequest request) {
        request.setFire_base_token(fireBaseToken);
        return apiInterface.requestAgentLogin(request);
    }

    public Observable<LoginResponse> requestLogin(LoginRequest request) {
        request.setFire_base_token(fireBaseToken);
        return apiInterface.requestLogin(request);
    }

    public Observable<ClientProfileResponse> requestActivateAccount() {
        return apiInterface.requestActivateAccount();
    }


    public Observable<RegisterResponse> requestCompleteData(CompleteDataRequest request) {
        request.setFire_base_token(fireBaseToken);


        if (request.getImage() != null)
            return apiInterface.requestCompleteData(
                    request.getCountry_id(),
                    request.getName(),
                    request.getMobile(),
                    request.getPassword(),
                    request.getFire_base_token(),
                    request.getImage()
            );
        else
            return apiInterface.requestCompleteData(request);

    }


    public Observable<AddressResponse> requestAddressName(LatLng latLng, Context context) throws IOException {
        AddressResponse addressResponse = new AddressResponse();

        Geocoder geocoder = new Geocoder(context, new Locale(new PreferencesManager(context).getLanguage()));
        try {
            if (Geocoder.isPresent()) {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                addressResponse.setAddress(addresses.get(0));
                addressResponse.setSuccess(true);
            }

            return Observable.just(addressResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return Observable.just(new AddressResponse());

        }
    }


    public Observable<RegisterResponse> requestAgentRegister(AgentRegisterRequest request) {
        request.setFire_base_token(fireBaseToken);
        return apiInterface.requestAgentRegister(
                request.getMobile(),
                request.getCountry_id(),
                request.getName(),
                request.getIdentity_name(),
                request.getIdentity_id(),
                request.getEmail(),
                request.getBirthday_date(),
                request.getNationality_id(),
                request.getCompany_id(),
                request.getTime_type(),
                request.getStc_pay_number(),
                request.getFire_base_token(),
                request.getImage(),
                request.getIdentity_image(),
                request.getDriving_license_image(),
                request.getCar_insurance_image());
    }

    public Observable<BanksResponse> requestBanks() {
        return apiInterface.requestBanks();
    }

    public Observable<NationalitiesResponse> requestNationalities() {
        return apiInterface.requestNationalities();
    }


    public Observable<BankAccountResponse> requestBankAccountDetails() {
        return apiInterface.requestBankAccountDetails();
    }

    public Observable<BaseResponse> requestEditBankAccount(EditBankAccountRequest request) {
        return apiInterface.requestEditBankAccount(request);
    }

    public Observable<BaseResponse> requestAddBankAccount(AddBankAccountRequest request) {
        return apiInterface.requestAddBankAccount(request);
    }

    public Observable<TermsAndConditionsResponse> requestTermsAndConditions(int user_type_id) {
        return apiInterface.requestTerms(user_type_id);
    }

    public Observable<TicketsResponse> requestTickets() {
        return apiInterface.requestTickets();
    }

    public Observable<BaseResponse> requestDeleteTicket(int ticketId) {
        return apiInterface.requestDeleteTicket(ticketId);
    }


    public Observable<BaseResponse> requestAgentAddTicket(AddTicketRequest request) {
        if (request.getImages() != null) {
            return apiInterface.requestAgentAddTicket(
                    request.getTicket_type_id(),
                    request.getName(),
                    request.getMobile(),
                    request.getMessage(),
                    request.getImages());
        } else {
            return apiInterface.requestAgentAddTicketWithoutImages(
                    request.getTicket_type_id(),
                    request.getName(),
                    request.getMobile(),
                    request.getMessage());
        }
    }

    public Observable<BaseResponse> requestClientAddTicket(AddTicketRequest request) {
        if (request.getImages() != null) {
            return apiInterface.requestClientAddTicket(
                    request.getSupport_section_id(),
                    request.getName(),
                    request.getMobile(),
                    request.getEmail(),
                    request.getMessage(),
                    request.getImages());
        } else {
            return apiInterface.requestClientAddTicketWithoutImages(
                    request.getSupport_section_id(),
                    request.getName(),
                    request.getMobile(),
                    request.getEmail(),
                    request.getMessage());
        }
    }

    public Observable<TicketTypesResponse> requestTicketTypes() {
        return apiInterface.requestTicketTypes();
    }

    public Observable<NotificationsResponse> requestNotifications() {
        return apiInterface.requestNotifications();
    }

    public Observable<BaseResponse> requestSeenNotifications() {
        return apiInterface.requestSeenNotifications();
    }

    public Observable<OrderDetailsResponse> requestOrderDetails(int orderId) {
        return apiInterface.requestOrderDetails(orderId);
    }

    public Observable<BaseResponse> requestSendOffer(SendOfferRequest request) {
        return apiInterface.requestSendOffer(request);
    }

    public Observable<BaseResponse> requestFinishOrder(int orderId) {
        return apiInterface.requestFinishOrder(orderId);
    }

    public Observable<CancelOrderReasonsResponse> requestAgentCancelOrderReasons() {
        return apiInterface.requestAgentCancelOrderReasons();
    }

    public Observable<BaseResponse> requestAgentCancelOrder(CancelOrderRequest request) {
        return apiInterface.requestAgentCancelOrder(request);
    }

    public Observable<BaseResponse> requestRateClient(RateClientRequest request) {
        return apiInterface.requestRateClient(request);
    }

    public Observable<BaseResponse> requestRateAgent(RateAgentRequest request) {
        return apiInterface.requestRateAgent(request);
    }


    public Observable<ProductDetailsResponse> requestProductDetails(ProductDetailsRequest request) {
        return apiInterface.requestProductDetails(request);

    }

    public Observable<OrderDetailsResponse> requestMakeOrder(MakeOrderRequest request) {
        return apiInterface.requestMakeOrder(request);
    }

    public Observable<ValueAddedTaxResponse> requestValueAddedTax() {
        return apiInterface.requestValueAddedTax();
    }

    public Observable<CouponResponse> requestCoupon(String coupon) {
        return apiInterface.requestCoupon(coupon);
    }

    public Observable<FavoritePlacesResponse> requestFavoritePlaces(int page) {
        return apiInterface.requestFavoritePlaces(page);
    }

    public Observable<BaseResponse> requestRemoveFavoritePlace(int favorite_place_id) {
        return apiInterface.requestRemoveFavoritePlace(favorite_place_id);
    }

    public Observable<BaseResponse> requestAddAddress(AddFavoritePlacesRequest request) {
        return apiInterface.requestAddAddress(request);
    }

    public Observable<PaymentMethodsResponse> requestPaymentMethods() {
        return apiInterface.requestPaymentMethods();
    }

    public Observable<CancelOrderReasonsResponse> requestClientCancelOrderReasons() {
        return apiInterface.requestClientCancelOrderReasons();
    }

    public Observable<BaseResponse> requestUserCancelOrder(CancelOrderRequest request) {
        return apiInterface.requestUserCancelOrder(request);
    }

    public Observable<CompanyProfileResponse> requestCompanyProfile() {
        return apiInterface.requestCompanyProfile();
    }

    public Observable<AgentDetailsResponse> requestAgentDetails(String agentId) {
        return apiInterface.requestAgentDetails(agentId);
    }

    public Observable<BaseResponse> requestAcceptsAgent(AcceptAgentRequest request) {
        return apiInterface.requestAcceptsAgent(request);
    }

    public Observable<BaseResponse> requestRejectAgent(RejectAgentRequest request) {
        return apiInterface.requestRejectAgent(request);
    }

    public Observable<BaseResponse> requestIgnoreOrder(int orderId) {
        return apiInterface.requestIgnoreOrder(orderId);
    }

    public Observable<ForgetPasswordResponse> requestForgetPassword(ForgetPasswordRequest request) {
        return apiInterface.requestForgetPassword(request);
    }


    public Observable<BaseResponse> requestChangeForgottenPassword(String password) {
        return apiInterface.requestChangeForgottenPassword(password);
    }


    public Observable<StatusesResponse> requestOrderStatuses(int orderId) {
        return apiInterface.requestOrderStatuses(orderId);
    }


    public Observable<BaseResponse> requestUpdateOrderStatus(UpdateOrderStatusRequest request) {
        return apiInterface.requestUpdateOrderStatus(request);
    }


    public Observable<FinancialMovementsResponse> requestFinancialMovements() {
        return apiInterface.requestFinancialMovements();
    }


    public Observable<PaymentResponse> requestPayment(PaymentRequest request) {
        return apiInterface.requestPayment(request);


    }

    public Observable<BaseResponse> requestChangePaymentType(ChangePaymentTypeRequest request) {
        return apiInterface.requestChangePaymentType(request);


    }

    public Observable<OffersResponse> requestOffers() {
        return apiInterface.requestOffers();
    }

    public Observable<TermsAndConditionsResponse> requestTerms(int userTypeId) {
        return apiInterface.requestTerms(userTypeId);
    }

    public Observable<TransactionsResponse> requestTransactions() {
        return apiInterface.requestTransactions();
    }

    public Observable<WalletResponse> requestWallet() {
        return apiInterface.requestWallet();
    }

    public Observable<PaymentCardsResponse> requestPaymentCards() {
        return apiInterface.requestPaymentCards();
    }

    public Observable<BaseResponse> requestDeletePaymentCard(int paymentId) {
        return apiInterface.requestDeletePaymentCard(paymentId);
    }

    public Observable<PaymentCardResponse> requestAddPaymentCard(AddPaymentCardRequest request) {
        return apiInterface.requestAddPaymentCard(request);


    }

    public Observable<CouponsResponse> requestCoupons() {
        return apiInterface.requestCoupons();
    }


    public Observable<BaseResponse> requestAddCoupon(String code) {
        return apiInterface.requestAddCoupon(code);
    }


    public Observable<OrdersResponse> requestBills() {
        return apiInterface.requestBills();
    }


    public Observable<SubjectsResponse> requestSubjects() {
        return apiInterface.requestSubjects();
    }

    public Observable<SupportResponse> requestSupport() {
        return apiInterface.requestSupport();
    }


    public Observable<BaseResponse> requestSuggest(SuggestRequest request) {
        return apiInterface.requestSuggest(request);
    }

    public Observable<TicketsResponse> requestClientTickets() {
        return apiInterface.requestClientTickets();
    }

    public Observable<ReplyResponse> requestClientReply(int supportTicketId) {
        return apiInterface.requestClientReply(supportTicketId);
    }


    public Observable<BaseResponse> requestAddTicketReply(AddTicketReplyRequest request) {
        if (request.getImages() != null) {
            return apiInterface.requestAddTicketReply(
                    request.getSupport_ticket_id(),
                    request.getReply(),
                    request.getImages());
        } else {
            return apiInterface.requestAddTicketReplyWithoutImages(
                    request.getSupport_ticket_id(),
                    request.getReply());
        }
    }

    public Observable<BaseResponse> requestCharge(ChargeRequest request) {
        return apiInterface.requestCharge(request);
    }

    public Observable<PaymentResponse> requestChargeBalance(ChargeBalanceRequest addTicketRequest) {
        return apiInterface.requestChargeBalance(addTicketRequest);
    }


    public Observable<AuthResponse> requestUpdateImage(MultipartBody.Part image) {
        return apiInterface.requestUpdateImage(image);
    }


    public Observable<BaseResponse> requestAgentAcceptOrder(int orderId) {
        return apiInterface.requestAgentAcceptOrder(orderId);

    }

    public Observable<BaseResponse> requestMessageNotification(MessageNotificationRequest request) {
        return apiInterface.requestMessageNotification(request);

    }

    public Observable<BaseResponse> requestSendChatFile(ChatFileRequest request) {
        return apiInterface.requestSendChatFile(
                request.getChat_id(),
                request.getFile_type(),
                request.getReply_key(),
                request.getFile());

    }

    public Observable<CompaniesResponse> requestCompanies() {
        return apiInterface.requestCompanies();
    }


    public Observable<List<CartProduct>> getAllProducts() {
        Timber.d("ShadyDB getAll: db.getAll");
        return Observable.fromCallable(() -> db.productDao().getAllProducts());
    }

    public Observable<Integer> deleteProductById(int cartProductId) {
        Timber.d("ShadyDB getAll: db.deleteById");

        return Observable.fromCallable(() -> db.productDao().deleteProductById(cartProductId));
    }

    public Observable<Float> getTotalProductsCost() {
        Timber.d("ShadyDB getAll: db.getTotalCost");

        return Observable.fromCallable(() -> db.productDao().getTotalProductsCost());
    }

    public Observable<Integer> deleteAllProducts() {
        Timber.d("ShadyDB getAll: db.deleteAll");

        return Observable.fromCallable(() -> db.productDao().deleteAll());
    }

    public Observable<Integer> updateProductCountById(int id, int count) {
        Timber.d("ShadyDB getAll: db.updateCountById");

        return Observable.fromCallable(() -> db.productDao().updateProductCountById(id, count));
    }

    public Observable<List<CartProduct>> findSameProduct(SameProductRequest request) {
        Timber.d("ShadyDB getAll: db.findById");

        return Observable.fromCallable(() -> db.productDao().findSameProduct(
                request.getProduct_id(),
                request.getType_id(),
                request.getSize_id(),
                request.getExtras_ids(),
                request.getChoices_ids()
        ));
    }

    public Observable<List<CartProduct>> requestIsAnotherStoreExist(int storeId) {
        Timber.d("ShadyDB getAll: db.findById");

        return Observable.fromCallable(() -> db.productDao().requestIsAnotherStoreExist(storeId));
    }


    public Observable<Long> InsertProduct(CartProduct cartProduct) {
        Timber.d("ShadyDB getAll: db.InsertProduct");
        return Observable.fromCallable(() -> db.productDao().InsertProduct(cartProduct));
    }

    public Observable<Integer> requestProductCount(int productId) {
        return Observable.fromCallable(() -> db.productDao().requestProductCount(productId));
    }

    public Observable<PaymentResponse> requestValidateCard(AddPaymentRequest key) {
        return apiInterface.requestValidateCard(key);
    }

    public Observable<BaseResponse> requestCardValid(int credit_card_id) {
        return apiInterface.requestCardValid(credit_card_id);
    }
}
