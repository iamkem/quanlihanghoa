package sample.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Main;
import sample.models.ImageData;
import sample.models.NhaCC;

import static sample.controllers.Controller.daluuchua;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuanLyNhaCungCapController {
    public ComboBox cbquocgia;
    public ImageView imvbieutuong;
    public TextField tftenncc;
    public Button bthuy;
    public Button btcapnhat;
    public Button btthemmoincc;
    public Button btxoa;

    public void btthemmoinccClick(ActionEvent actionEvent) {
        try {
            if (!Main.temp_data.isExistNhaCC(tftenncc.getText().trim())) {
                NhaCC ncc = new NhaCC(Main.temp_data.getNextIDNCC(), tftenncc.getText(),
                        cbquocgia.getSelectionModel().getSelectedItem().toString(), new ImageData(imvbieutuong.getImage(), "png"));
                Main.temp_data.getNhacc().add(ncc);
                Controller.showAlert("Thêm Nhà Cung Cấp", "Nhà cung cấp đã được thêm thành công!", Alert.AlertType.INFORMATION);
                ((Stage) btthemmoincc.getScene().getWindow()).close();
                daluuchua = false;
            } else {
                Controller.showAlert("Thêm Nhà Cung Cấp", "Lỗi! Tên nhà cung cấp này đã tồn tại HOẶC không được phép dùng, xin vui lòng nhập tên khác!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            Controller.showAlert("Thêm Nhà Cung Cấp", "Lỗi! Xin vui lòng kiểm tra lại dữ liệu đã nhập!", Alert.AlertType.ERROR);
        }
    }

    public ArrayList<String> getCountries() {
        ArrayList<String> list = new ArrayList<>();
        String[] locale = Locale.getISOCountries();
        for (String country : locale)
            list.add((new Locale("vi", country)).getDisplayCountry(new Locale("vi", "vn")));
        Collections.sort(list);
        return list;
    }

    public void loadCountries(MouseEvent mouseEvent) {
        if (cbquocgia.getItems().isEmpty())
            cbquocgia.setItems(FXCollections.observableArrayList(getCountries()));
    }

    public void imvbieutuongClick(MouseEvent mouseEvent) throws FileNotFoundException {
        Main.selectImage(imvbieutuong);
    }

    public void imvbieutuongMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install((ImageView) mouseEvent.getTarget(), new Tooltip("Nhấp Chuột Để Chọn Ảnh"));
    }

    public void capNhatTuDong() {
        NhaCC ncc = (NhaCC) Main.obj;
        tftenncc.setText(ncc.getTen_ncc());
        try {
            imvbieutuong.setImage(ncc.getHinhanh().readImageFromArray());
        } catch (IOException e) {
        }
    }

    public void bthuy(ActionEvent actionEvent) {
        ((Stage) bthuy.getScene().getWindow()).close();
    }

    public void btcapnhatClick(ActionEvent actionEvent) {
        try {
            NhaCC ncc = (NhaCC) Main.obj;
            Main.temp_data.getNhaCCAt(ncc.getId_ncc()).setTen_ncc(this.tftenncc.getText());
            Main.temp_data.getNhaCCAt(ncc.getId_ncc()).setQuocgia_ncc(this.cbquocgia.getSelectionModel().getSelectedItem().toString());
            Main.temp_data.getNhaCCAt(ncc.getId_ncc()).setHinhanh(new ImageData(this.imvbieutuong.getImage(), "png"));
            Controller.updatedata = 2;
            Controller.showAlert("Cập Nhật Nhà Cung Cấp", "Nhà Cung Cấp đã được cập nhật thành công", Alert.AlertType.INFORMATION);
            ((Stage) btcapnhat.getScene().getWindow()).close();
            daluuchua = false;
        } catch (IOException e) {
            Controller.showAlert("Quản Lý Nhà Cung Cấp", "Lỗi" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void btxoaClick(ActionEvent actionEvent) {
        ButtonType bttype = Controller.showAlert("Xoá?", "Bạn có muốn xoá: " + tftenncc.getText() + "?", Alert.AlertType.CONFIRMATION);
        if (bttype == ButtonType.OK) {
            Main.temp_data.getNhacc().remove(Main.obj);
            Controller.showAlert("Xoá Nhà Cung Cấp", "Xoá Thành Công!", Alert.AlertType.INFORMATION);
            ((Stage) btxoa.getScene().getWindow()).close();
            Controller.daluuchua = false;
        }
    }
}