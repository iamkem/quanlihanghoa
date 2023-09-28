package sample.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;
import sample.models.KhachHang;

import static sample.controllers.Controller.daluuchua;

import java.time.LocalDate;

public class QuanLyKhachHangController {
    public TextField tftenkh;
    public TextField tfsdt;
    public TextArea txtdiachi;
    public DatePicker dprnamsinh;
    public ComboBox<String> cbgioitinh;
    public Button btthemmoi;
    public Button btcapnhat;
    public Button btxoa;
    public Button bthuy;

    public void btthemmoiClick(ActionEvent actionEvent) {
        try {
            if (!Main.temp_data.isExistKhachHang(tfsdt.getText().trim())) {
                String ten_kh = tftenkh.getText();
                String sdt_kh = tfsdt.getText();
                String diachi_kh = txtdiachi.getText();
                LocalDate ntns_kh = dprnamsinh.getValue();
                String gt_kh = cbgioitinh.getSelectionModel().getSelectedItem();
                KhachHang kh = new KhachHang(Main.temp_data.getNextIDKhachHang(), ten_kh, sdt_kh, diachi_kh, ntns_kh, gt_kh);
                Main.temp_data.getKhachang().add(kh);
                Controller.showAlert("Thêm Khách Hàng", "Khách Hàng đã được thêm thành công!", Alert.AlertType.INFORMATION);
                ((Stage) btthemmoi.getScene().getWindow()).close();
                daluuchua = false;
            } else {
                Controller.showAlert("Thêm Khách Hàng", "Lỗi! Khách hàng với Số Điện Thoại này đã tồn tại HOẶC không được phép dùng!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            Controller.showAlert("Thêm Khách Hàng", "Lỗi! Xin vui lòng kiểm tra lại dữ liệu đã nhập!", Alert.AlertType.ERROR);
        }
    }

    //Hiển thị dữ liệu từ hàng hóa được chọn lên hộp thoại sửa dữ liệu
    public void capNhatTuDong() {
        try {
            if (Main.obj == null) {
                btcapnhat.setDisable(true);
                btxoa.setDisable(true);
            }
            KhachHang model = (KhachHang) Main.obj;
            tftenkh.setText(model.getTen_kh());
            tfsdt.setText(model.getSdt_kh() + "");
            txtdiachi.setText(model.getDiachi_kh());
            dprnamsinh.setValue(model.getNtns_kh());
            cbgioitinh.getSelectionModel().select(model.getGt_kh());
        } catch (Exception e) {
        }

    }

    public void btcapnhatClick(ActionEvent actionEvent) {
        try {
            KhachHang model = (KhachHang) Main.obj;
            model.setTen_kh(tftenkh.getText());
            model.setSdt_kh(tfsdt.getText());
            model.setDiachi_kh(txtdiachi.getText());
            model.setNtns_kh(dprnamsinh.getValue());
            model.setGt_kh(cbgioitinh.getSelectionModel().getSelectedItem());
            Controller.updatedata = 4;//Đã có sự thay đổi dữ liệu của bảng Khách Hàng
            Controller.showAlert("Cập Nhật Khách Hàng", "Khách Hàng đã được cập nhật thành công", Alert.AlertType.INFORMATION);
            ((Stage) btcapnhat.getScene().getWindow()).close();
            daluuchua = false;
        } catch (Exception e) {
            Controller.showAlert("Quản Lý Khách Hàng", "Lỗi" + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    public void bthuy(ActionEvent actionEvent) {
        ((Stage) bthuy.getScene().getWindow()).close();
    }

    public void btxoaCLick(ActionEvent actionEvent) {
        ButtonType bttype = Controller.showAlert("Xoá?", "Bạn có muốn xoá: " + tftenkh.getText() + "?", Alert.AlertType.CONFIRMATION);
        if (bttype == ButtonType.OK) {
            Main.temp_data.getKhachang().remove(Main.obj);
            Controller.showAlert("Xoá Nhà Cung Cấp", "Xoá Thành Công!", Alert.AlertType.INFORMATION);
            ((Stage) btxoa.getScene().getWindow()).close();
            Controller.daluuchua = false;
        }
    }
}