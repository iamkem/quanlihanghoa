package sample.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.omg.CosNaming.NameHelper;
import sample.Main;
import sample.models.NhanVien;


import static sample.controllers.Controller.daluuchua;

public class QuanLyNhanVienController {
    public TextField tftennv;
    public TextField tfsdt;
    public TextField tfmk;
    public ComboBox<String> cbgioitinh;
    public Button bthuy;
    public Button btcapnhat;
    public Button btthemmoinv;
    public Button btxoa;

    public void btthemmoiClick(ActionEvent actionEvent) {
        try {
            if (!Main.temp_data.isExistNhanVien(tfsdt.getText().trim())) {
                int id_nv = Main.temp_data.getNextIDNhanVien();
                String hoten_nv = tftennv.getText();
                String sdt_nv = tfsdt.getText();
                String mk_nv = tfmk.getText();
                String gt_nv = cbgioitinh.getSelectionModel().getSelectedItem();
                NhanVien nv = new NhanVien(id_nv, hoten_nv, sdt_nv, mk_nv, gt_nv);
                Main.temp_data.getNhanvien().add(nv);
                Controller.showAlert("Thêm Nhân Viên", "Nhân viên đã được thêm thành công!", Alert.AlertType.INFORMATION);
                ((Stage) btthemmoinv.getScene().getWindow()).close();
                daluuchua = false;
            } else {
                Controller.showAlert("Thêm Nhân Viên", "Lỗi! Số Điện Thoại này đã tồn tại HOẶC không hợp lệ!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            Controller.showAlert("Thêm Nhân Viên", "Lỗi! Xin vui lòng kiểm tra lại dữ liệu được nhập!", Alert.AlertType.ERROR);
        }
    }

    public void capNhatTuDong() {
        NhanVien nv = (NhanVien) Main.obj;
        tftennv.setText(nv.getHoten_nv());
        tfsdt.setText(nv.getSdt_nv());
        tfmk.setText(nv.getMk_nv());
        cbgioitinh.getSelectionModel().select(nv.isGt_nv());
    }

    public void bthuy(ActionEvent actionEvent) {
        ((Stage) bthuy.getScene().getWindow()).close();
    }

    public void btcapnhatClick(ActionEvent actionEvent) {
        try {
            NhanVien nv = (NhanVien) Main.obj;
            Main.temp_data.getNhanViengAt(nv.getId_nv()).setHoten_nv(tftennv.getText());
            Main.temp_data.getNhanViengAt(nv.getId_nv()).setSdt_nv(tfsdt.getText());
            Main.temp_data.getNhanViengAt(nv.getId_nv()).setMk_nv(tfmk.getText());
            Main.temp_data.getNhanViengAt(nv.getId_nv()).setGt_nv(cbgioitinh.getSelectionModel().getSelectedItem());
            Controller.updatedata = 3;
            Controller.showAlert("Cập Nhật Nhân Viên", "Cập nhật thành công!", Alert.AlertType.INFORMATION);
            ((Stage) btcapnhat.getScene().getWindow()).close();
            daluuchua = false;
        } catch (Exception e) {
            Controller.showAlert("Quản Lý Nhân Viên", "Lỗi" + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    public void btxoaClick(ActionEvent actionEvent) {
        ButtonType bttype = Controller.showAlert("Xoá?", "Bạn có muốn xoá: " +tftennv.getText() + "?", Alert.AlertType.CONFIRMATION);
        if (bttype == ButtonType.OK) {
            Main.temp_data.getNhanvien().remove(Main.obj);
            Controller.showAlert("Xoá Nhà Cung Cấp", "Xoá Thành Công!", Alert.AlertType.INFORMATION);
            ((Stage) btxoa.getScene().getWindow()).close();
            Controller.daluuchua = false;
        }
    }
}