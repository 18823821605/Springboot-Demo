package com.springboot.dlc.modules.controller.system;


import com.springboot.dlc.modules.controller.base.BaseController;
import com.springboot.dlc.common.exception.MyException;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.common.utils.QRCodeUtil;
import com.springboot.dlc.common.utils.QiniuUtil;
import com.springboot.dlc.common.utils.UploadFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/8/4
 * @Description: 文件上传
 **/
@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController extends BaseController {

    /**
     * @Description 文件上传
     * @Date 2018/7/11 20:33
     * @Author liangshihao
     */
    @PostMapping("/front/upload")
    public ResultView appFileUpload(@RequestParam("file") MultipartFile[] file,
                                    @RequestParam(value = "fileName", required = false, defaultValue = "images") String fileName) throws MyException {
        return ResultView.ok(UploadFileUtil.flowUpload(file, webResource.getStaticResourcePath(), fileName));
    }

    /**
     * @Description 文件上传
     * @Date 2018/7/11 20:33
     * @Author liangshihao
     */
    @PostMapping("/admin/upload")
    public ResultView adminFileUpload(@RequestParam("file") MultipartFile[] file,
                                      @RequestParam(value = "fileName", required = false, defaultValue = "images") String fileName) throws MyException {
        return ResultView.ok(UploadFileUtil.flowUpload(file, webResource.getStaticResourcePath(), fileName));
    }

    /**
     * 生成设备二维码
     */
    @GetMapping("/front/downloadQr")
    public void downloadQr(String equipmentNumber, HttpServletResponse response) {
        QRCodeUtil createQrcode = new QRCodeUtil();
        //生成二维码
        try {
            createQrcode.getQrcode(webResource.getEquipmentPath() + equipmentNumber, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方七牛上传
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/front/fileUploadQiniu")
    public ResultView fileUploadQiniu(@RequestParam(value = "file") MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            return ResultView.ok(QiniuUtil.upLoadImage(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
