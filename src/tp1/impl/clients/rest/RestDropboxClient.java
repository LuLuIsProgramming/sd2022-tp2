package tp1.impl.clients.rest;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import jakarta.ws.rs.core.GenericType;
import org.pac4j.scribe.builder.api.DropboxApi20;
import tp1.api.service.java.Result;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

public class RestDropboxClient extends RestClient {

    private static final String apiKey = "7h0hejx1sgw3gh3";
    private static final String apiSecret = "4cocm7rs5s1bb3v";
    private static final String accessTokenStr = "sl.BI_w56F8Th8Zrr3ln3oudKpm9-qbndmxqTiIpzJCIrPfBYFhmLWscCqniSPIjDeUo8yBOKav4oKkJ7TeQwyUN6Vx80sske_eXvIinpVoN5ib8qK6Te3-7wC9x-2ep_sN1DixYgc";


    private static final String UPLOAD_FILE_URL = "https://api.dropboxapi.com/2/files/upload";
    private static final String DOWNLOAD_FILE_URL = "https://api.dropboxapi.com/2/files/create_folder_v2";
    private static final String DELETE_FILE_V2_URL = "https://api.dropboxapi.com/2/files/delete_v2";

    private static final int HTTP_SUCCESS = 200;
    private static final String DROPBOX_API_ARG = "Dropbox-API-Arg";
    private static final String CONTENT_TYPE_HDR = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String OCTET_STREAM = "application/octet-stream";

    private final Gson json;
    private final OAuth20Service service;
    private final OAuth2AccessToken accessToken;

    public RestDropboxClient(URI uri, String path) {
        super(uri, path);
        json = new Gson();
        accessToken = new OAuth2AccessToken(accessTokenStr);
        service = new ServiceBuilder(apiKey).apiSecret(apiSecret).build(DropboxApi20.INSTANCE);
    }

    public Result<Void> upload(String fileId, byte[] data) throws Exception{
        var uploadFile = new OAuthRequest(Verb.POST, UPLOAD_FILE_URL);
        uploadFile.addHeader(DROPBOX_API_ARG, json.toJson(new UploadArg(false, "add", false, fileId, false)));
        uploadFile.addHeader(CONTENT_TYPE_HDR, OCTET_STREAM);

        uploadFile.setPayload(data);
        service.signRequest(accessToken, uploadFile);

        //
        return reTry(() -> {
            try {
                Response r = service.execute(uploadFile);
                if (r.getCode() != HTTP_SUCCESS)
                    return Result.error(Result.ErrorCode.INTERNAL_ERROR);
                return Result.ok();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        });
    }

    public Result<byte[]> download(String fileId) throws Exception{
        var downloadFile = new OAuthRequest(Verb.POST, DOWNLOAD_FILE_URL);
        downloadFile.addHeader(DROPBOX_API_ARG, json.toJson(new DownloadArg(fileId)));
        downloadFile.setPayload(json.toJson(new DeleteArg(fileId)));
        service.signRequest(accessToken, downloadFile);

        return reTry(() -> {
            try {
                Response r = service.execute(downloadFile);
                if (r.getCode() != HTTP_SUCCESS)
                    return Result.error(Result.ErrorCode.INTERNAL_ERROR);
                return Result.ok(r.getStream().readAllBytes());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        });
    }

    public Result<Void> delete(String fileId) throws Exception{
        var deleteFile = new OAuthRequest(Verb.POST, DELETE_FILE_V2_URL);
        deleteFile.addHeader(CONTENT_TYPE_HDR, JSON_CONTENT_TYPE);

        deleteFile.setPayload(json.toJson(new DeleteArg(fileId)));
        service.signRequest(accessToken, deleteFile);

        return reTry(() -> {
            try {
                Response r = service.execute(deleteFile);
                if (r.getCode() != HTTP_SUCCESS)
                    return Result.error(Result.ErrorCode.INTERNAL_ERROR);
                return Result.ok();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        });

    }
}

record UploadArg(boolean autorename, String mode, boolean mute, String path, boolean strict_conflict){}
record DeleteArg(String path){}
record DownloadArg(String path){}