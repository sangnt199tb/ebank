package fundtransfer.presentation.service.impl;

import fundtransfer.presentation.model.TransferRequest;
import fundtransfer.presentation.service.FundTransferService;
import fundtransfer.presentation.util.SecurityContextUtil;
import org.springframework.stereotype.Service;

@Service
public class FundTransferServiceImpl implements FundTransferService {
    @Override
    public void transfer(TransferRequest requestBody) {
        // Cần CIF ở đâu thì gọi thẳng Class tiện ích ra lấy, cực kỳ sang chảnh!
        String currentCif = SecurityContextUtil.getCurrentCif();
        System.out.println("currentCif kq: " + currentCif);

        if (currentCif == null || currentCif.isEmpty()) {
            throw new RuntimeException("Không xác định được danh tính khách hàng!");
        }

        System.out.println("Xử lý trừ tiền cho CIF: " + currentCif);
    }
}
