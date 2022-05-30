package com.linkpcom.mitrafast.Classes.DI.Modules;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.ApiInterface;
import com.linkpcom.mitrafast.Classes.REST.Interceptors.HeadersInterceptor;
import com.linkpcom.mitrafast.Classes.REST.Interceptors.NetworkInterceptor;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.AgentNode;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AcceptedForOrderRequest;
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
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FirebaseOrderDetailsResponse;
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
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductCountResponse;
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
import com.visionstech.whatsappview.ChatNode;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;


@Module
@InstallIn(ViewModelComponent.class)
public class NetworkModule {

    @Provides
    @ViewModelScoped
    public ApiInterface provideApiInterface(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(CONSTANTS.API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiInterface.class);
    }


    @Provides
    @ViewModelScoped
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, HeadersInterceptor headersInterceptor, NetworkInterceptor networkInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .addInterceptor(headersInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    @Provides
    @ViewModelScoped
    public HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }


    @Provides
    public MutableLiveData<Boolean> provideBooleanMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<String> provideStringMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<Float> floatMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<Long> longMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<Integer> integerMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<LatLng> latLngMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<AgentNode> agentNodeMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<Location> locationMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<ChatNode> chatNodeMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<AcceptedForOrderRequest> acceptedForOrderRequestMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<AboutUsResponse> aboutUsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<AddressResponse> addressResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<AdsResponse> adsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<AgentDetailsResponse> agentDetailsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<AgentProfileResponse> agentProfileResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<AuthResponse> authResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<BankAccountDetailsResponse> bankAccountDetailsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    MutableLiveData<BankAccountResponse> bankAccountResponseMutableLiveData(){
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<BanksResponse> banksResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<PaymentCardResponse> paymentCardResponseMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<BaseResponse> baseResponseMutableLiveData(){
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<CancelOrderReasonsResponse> cancelOrderReasonsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<CategoriesResponse> categoriesResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ClientProfileResponse> clientProfileResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<CouponResponse> couponResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<CouponsResponse> couponsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<FAQResponse> faqResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<FavoritePlacesResponse> favoritePlacesResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<FinancialMovementsResponse> financialMovementsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ForgetPasswordResponse> forgetPasswordResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<IntroSlidersResponse> introSlidersResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<LoginResponse> loginResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<NationalitiesResponse> nationalitiesResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<NotificationsResponse> notificationsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<OffersResponse> offersResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<OrderDetailsResponse> orderDetailsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<OrdersResponse> ordersResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<PaymentCardsResponse> paymentCardsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<PaymentMethodsResponse> paymentMethodsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<PaymentResponse> paymentResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ProductCountResponse> productCountResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ProductDetailsResponse> productDetailsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ProductsResponse> productsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<RegisterResponse> registerResponseMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<CompanyProfileResponse> companyProfileResponseMutableLiveData(){
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ShopDetailsResponse> shopDetailsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ShopsResponse> shopsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<StatusesResponse> statusesResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<SubjectsResponse> subjectsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<SupportResponse> supportResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<TermsAndConditionsResponse> termsAndConditionsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<TicketsResponse> ticketsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }


    @Provides
    public MutableLiveData<ReplyResponse> replyResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<TicketTypesResponse> ticketTypesResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<TransactionsResponse> transactionsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<ValueAddedTaxResponse> valueAddedTaxResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<WalletResponse> walletResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<CompaniesResponse> companiesResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<FavoriteResponse> favoriteResponseMutableLiveData() {
        return new MutableLiveData<>();
    }
    @Provides
    public MutableLiveData<FirebaseOrderDetailsResponse> firebaseOrderDetailsResponseMutableLiveData() {
        return new MutableLiveData<>();
    }

}
