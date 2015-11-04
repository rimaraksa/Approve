package com.example.rimaraksa.approve.Unused;

//public class CreateContractFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener{
//
//    private Account account = Global.account;
//
//    private static final String TAG = CreateContractActivity.class.getSimpleName();
//
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//
//    private Location mLastLocation;
//
//    // Google client to interact with Google API
//    private GoogleApiClient mGoogleApiClient;
//
////    Components on layout
//    private EditText receiverField, subjectField, bodyField;
//    private Button sendButton;
//    private String receiver, subject, body;
//
//
//    public CreateContractFragment(){
//        account = Global.account;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_create_contract, container, false);
//
//        receiverField = (EditText) view.findViewById(R.id.TFReceiver);
//        subjectField = (EditText) view.findViewById(R.id.TFSubject);
//        bodyField = (EditText) view.findViewById(R.id.TFContractBody);
//
//        sendButton = (Button) view.findViewById(R.id.BSend);
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                receiver = receiverField.getText().toString();
//                subject = subjectField.getText().toString();
//                body = bodyField.getText().toString();
//
//                sendContract();
//            }
//        });
//
//        if (checkPlayServices()) {
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//        }
//
//        return view;
//
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    public void sendContract() {
//
//        String location = getLocation();
//
////        Incomplete fields
//        if (receiver.equals("") || subject.equals("") || body.equals("")) {
//            //popup message
//            Toast pass = Toast.makeText(getActivity(), "Required fields have not been completed!", Toast.LENGTH_SHORT);
//            pass.show();
//        } else {
//            //insert new contract
//            String contractKey = UUID.randomUUID().toString();
//            new CreateContract(getActivity(), account).execute(contractKey, account.getUsername(), receiver, subject, body, location, "waiting", Global.getDateTime());
//
//        }
//    }
//
//    private String getLocation(){
//        String location = "0.0;0.0";
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//            location = latitude + ";" + longitude;
//        }
//
//        return location;
//    }
//
//    /**
//     * Creating google api client object
//     * */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Method to verify google play services on the device
//     * */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }
//            else {
//                Toast.makeText(getActivity().getApplicationContext(),"This device is not supported.", Toast.LENGTH_LONG).show();
//                getActivity().finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        checkPlayServices();
//    }
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//
//        // Once connected with google api, get the location
//        getLocation();
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//
//
//}

