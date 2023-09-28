package sample.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;
import sample.models.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;

public class Controller {

    public Button btthemhang;
    public Button btmodl;
    public TableView<HangHoa> tbv_data;
    public TableView<DanhMucHang> tbv_dm;
    public TableView<NhaCC> tbv_ncc;
    public TableView<NhanVien> tbv_nhanvien;
    public MenuItem mnithemnv;
    public Button btthemnhanvien;
    public Tab tabnhanvien;
    public TableView<HangHoa> tbv_cart;
    public TableView<KhachHang> tbv_khachhang;
    public TableView<DonHang> tbv_hoadon;
    public Button btluudl;
    public Tab tabhoadon;
    public TabPane tabpane;
    public Tab tabgiohang;
    public ComboBox<DanhMucHang> cbdm;
    public ComboBox<NhaCC> cbncc;
    public TextField tfgiatu;
    public TextField tfgiaden;
    public TextField tftukhoa;
    private ArrayList<HangHoa> ketquatimkiem = new ArrayList<>();
    public static boolean daluuchua = true;

    public static int updatedata = -1;

    public Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), f -> {
                //Giới hạn chức năng của nhân viên
                if (DangNhapController.aidangdangnhap.compareTo("administrator") != 0) {
                    btmodl.setDisable(true);
                    mnithemnv.setDisable(true);
                    btthemhang.setDisable(true);
                    tabnhanvien.setDisable(true);
                    btthemnhanvien.setDisable(true);
                }
                if (DangNhapController.aidangdangnhap == "") {
                    btluudl.setDisable(true);
                }

                //Chỉ cập nhật lại dữ liệu khi có sự thay đổi
                if (ketquatimkiem.size() != tbv_data.getItems().size())
                    taiLaiDuLieu(0);
                if (Main.temp_data.getDanhmuchang().size() != tbv_dm.getItems().size())
                    taiLaiDuLieu(1);
                if (Main.temp_data.getNhacc().size() != tbv_ncc.getItems().size())
                    taiLaiDuLieu(2);
                if (Main.temp_data.getNhanvien().size() != tbv_nhanvien.getItems().size())
                    taiLaiDuLieu(3);
                if (Main.temp_data.getKhachang().size() != tbv_khachhang.getItems().size())
                    taiLaiDuLieu(4);
                int idnv = 0;
                try {
                    idnv = Integer.parseInt(DangNhapController.aidangdangnhap);
                } catch (Exception e) {
                }
                if (Main.temp_data.coBaoNhieuDHCuaNV(idnv) != tbv_hoadon.getItems().size())
                    taiLaiDuLieu(5);
                if (Main.giohang.size() != tbv_cart.getItems().size())
                    taiLaiDuLieu(6);

                if (updatedata >= 0) {
                    taiLaiDuLieu(updatedata);
                    updatedata = -1;
                }

                if (cbdm.getItems().size() != Main.temp_data.getDanhmuchang().size())
                    cbdm.setItems(FXCollections.observableArrayList(Main.temp_data.getDanhmuchang()));
                if (cbncc.getItems().size() != Main.temp_data.getDanhmuchang().size())
                    cbncc.setItems(FXCollections.observableArrayList(Main.temp_data.getNhacc()));
            })
    );


    public static String tenNhanVienDangNhap() {
        if (DangNhapController.aidangdangnhap.compareTo("administrator") == 0) {
            return "administrator";
        } else if (DangNhapController.aidangdangnhap.trim().compareTo("") != 0) {
            int idnv = Integer.parseInt(DangNhapController.aidangdangnhap);
            for (NhanVien nv : Main.temp_data.getNhanvien())
                if (nv.getId_nv() == idnv)
                    return nv.getHoten_nv();
        }
        return "";
    }

    public void btmodlClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        FileChooser filechooser = new FileChooser();
        filechooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
        filechooser.setTitle("Mở Dữ Liệu");
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Quản Lý Hàng Hóa *.qlh", "*.qlh"));
        File file = filechooser.showOpenDialog(null);
        if (file != null) {
            Main.file = file;
            Main.temp_data = DataSerialization.readData(Main.file);
            ketquatimkiem = Main.temp_data.getHanghoa();
            taiLaiDuLieu(0);//Tải dữ liệu cho bảng hàng hóa
            taiLaiDuLieu(1);//...cho bảng danh mục
            taiLaiDuLieu(2);//...cho bảng nhà cung cấp
            taiLaiDuLieu(3);//...cho bảng nhân viên
            taiLaiDuLieu(4);//...cho bảng khách hàng
            taiLaiDuLieu(5);//...cho bảng hóa đơn
        }
    }

    public void mnithemhangClick(ActionEvent actionEvent) throws IOException {
        hienThiThemMoiHang();
    }

    public void btthemhangClick(ActionEvent actionEvent) throws IOException {
        hienThiThemMoiHang();
    }

    private void hienThiThemMoiHang() throws IOException {
        Main.openOption("/sample/views/quanlyhanghoa.fxml", "/sample/styles/quanlyhanghoa.css",
                "Quản Lý Hàng Hóa", 425, 550, null).show();
    }

    public void btluudlClick(ActionEvent actionEvent) throws IOException {
        if (Main.file == null) {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Lưu Dữ Liệu");
            filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Quản Lý Hàng Hóa *.qlh", "*.qlh"));
            Main.file = filechooser.showSaveDialog(null);
            if (Main.file != null)
                DataSerialization.writeData(Main.file, Main.temp_data);
        } else {
            DataSerialization.writeData(Main.file, Main.temp_data);
            showAlert("Lưu Dữ Liệu", "Dữ liệu đã được lưu vào: " + Main.file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        }
        FileOutputStream out = new FileOutputStream("path.properties");
        Properties prop = new Properties();
        prop.setProperty("path", Main.file.getAbsolutePath());
        prop.store(out, null);
        out.close();
        daluuchua = true;
    }

    public void btthemdmClick(ActionEvent actionEvent) throws IOException {
        Main.openOption("/sample/views/quanlydanhmuc.fxml", "/sample/styles/quanlydanhmuc.css",
                "Quản Lý Danh Mục", 425, 230, null).show();
    }

    public void btthemnhaccClick(ActionEvent actionEvent) throws IOException {
        Main.openOption("/sample/views/quanlynhacungcap.fxml", "/sample/styles/quanlynhacungcap.css",
                "Quản Lý Nhà Cung Cấp", 425, 270, null).show();
    }

    public void taiLaiDuLieu(int tbl_index) {
        switch (tbl_index) {
            case 0:
                //Tải dữ liệu cho bảng Hàng Hóa
                taiDuLieuChoBangHangHoa(ketquatimkiem);
                break;
            case 1:
                //Tải dữ liệu cho bảng danh mục
                taiDuLieuChoBangDanhMuc();
                break;
            case 2:
                //Tải dữ liệu cho bảng nhà cung cấp
                taiDuLieuChoBangNhaCC();
                break;
            case 3:
                //Tải dữ liệu cho bảng nhân viên
                taiDuLieuChoBangNhanVien();
                break;
            case 4:
                //Tải dữ liệu cho bảng khách hàng
                taiDuLieuChoBangKhachHang();
                break;
            case 5:
                //Tải dữ liệu cho bảng đơn hàng
                taiDuLieuChoBangDonHang();
                break;
            case 6:
                //Tải dữ liệu cho giỏ hàng
                taiDuLieuChoBangGioHang();
                break;
        }
    }

    private void taiDuLieuChoBangDonHang() {
        tbv_hoadon.getItems().clear();
        tbv_hoadon.getColumns().clear();
        ObservableList<DonHang> obldh;
        //Nếu admin đăng nhập, thì liệt kê toàn bộ hóa đơn. Nếu là nhân viên, thì chỉ liệt kê hóa đơn do họ tạo ra
        if (DangNhapController.aidangdangnhap.toLowerCase().compareToIgnoreCase("administrator") == 0)
            obldh = FXCollections.observableArrayList(Main.temp_data.getDonhang());
        else {
            ArrayList<DonHang> listdh = new ArrayList<>();
            for (DonHang dh : Main.temp_data.getDonhang())
                if (dh.getId_nv() == Integer.parseInt(DangNhapController.aidangdangnhap))
                    listdh.add(dh);
            obldh = FXCollections.observableArrayList(listdh);
        }
        TableColumn tbcid_dh = new TableColumn("ID Đơn Hàng");
        tbcid_dh.setCellValueFactory(new PropertyValueFactory<DonHang, Integer>("id_mh"));
        TableColumn tbcthoigian_mh = new TableColumn("Thời Gian Mua");
        tbcthoigian_mh.setCellValueFactory(new PropertyValueFactory<DonHang, LocalDateTime>("thoigian_mh"));
        TableColumn tbcten_kh = new TableColumn("Tên Khách Hàng");
        tbcten_kh.setCellValueFactory(new PropertyValueFactory<DonHang, Integer>("id_kh"));
        tbcten_kh.setCellFactory(param -> new TableCell<DonHang, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    KhachHang kh = Main.temp_data.getKhachHangAt(item);
                    setText(kh.getTen_kh());
                }
            }
        });
        tbv_hoadon.setItems(obldh);
        tbv_hoadon.getColumns().addAll(tbcid_dh, tbcthoigian_mh, tbcten_kh);
    }

    private void taiDuLieuChoBangKhachHang() {
        tbv_khachhang.getItems().clear();
        tbv_khachhang.getColumns().clear();
        ObservableList<KhachHang> oblkh = FXCollections.observableArrayList(Main.temp_data.getKhachang());
        TableColumn tbcid_kh = new TableColumn("ID Khách Hàng");
        tbcid_kh.setCellValueFactory(new PropertyValueFactory<KhachHang, Integer>("id_kh"));
        TableColumn tbcten_kh = new TableColumn("Họ Tên");
        tbcten_kh.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("ten_kh"));
        TableColumn tbcsdt_kh = new TableColumn("Số Điện Thoại");
        tbcsdt_kh.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("sdt_kh"));
        TableColumn tbcdiachi_kh = new TableColumn("Địa Chỉ");
        tbcdiachi_kh.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("diachi_kh"));
        TableColumn tbcntns_kh = new TableColumn("Năm Sinh");
        tbcntns_kh.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("ntns_kh"));
        TableColumn tbcgt_kh = new TableColumn("Giới Tính");
        tbcgt_kh.setCellValueFactory(new PropertyValueFactory<KhachHang, String>("gt_kh"));

        tbv_khachhang.setItems(oblkh);
        tbv_khachhang.getColumns().addAll(tbcid_kh, tbcten_kh, tbcsdt_kh, tbcdiachi_kh, tbcntns_kh, tbcgt_kh);
    }

    boolean start_schedule = false;

    public void capNhatLaiDuLieu(MouseEvent mouseEvent) {
        if (start_schedule == false) {
            start_schedule = true;
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        ((Stage) ((BorderPane) mouseEvent.getSource()).getScene().getWindow()).setTitle("Quản Lý Bán Hàng [" + tenNhanVienDangNhap() + "]");
    }

    public void taiDuLieuChoBangHangHoa(ArrayList<HangHoa> ketqua) {
        tbv_data.getItems().clear();
        tbv_data.getColumns().clear();
        ObservableList<HangHoa> oblhanghoa = FXCollections.observableArrayList(ketqua);
        TableColumn tbcid_hh = new TableColumn("ID Hàng Hóa");
        tbcid_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Integer>("id_hh"));
        TableColumn tbcten_hh = new TableColumn("Tên Hàng Hóa");
        tbcten_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, String>("ten_hh"));
        TableColumn tbcsoluong_hh = new TableColumn("Số Lượng Hàng Hóa");
        tbcsoluong_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Integer>("soluong_hh"));
        TableColumn tbcgiatien_hh = new TableColumn("Giá Hàng Hóa");
        tbcgiatien_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Integer>("giatien_hh"));
        TableColumn tbcmota_hh = new TableColumn("Mô Tả Hàng Hóa");
        tbcmota_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, String>("mota_hh"));
        TableColumn tbchinhanh_hh = new TableColumn("Hình Ảnh Hàng Hóa");
        tbchinhanh_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, ImageData>("hinhanh_hh2"));
        tbchinhanh_hh.setCellFactory(p -> new TableCell<HangHoa, ImageData>() {
            @Override
            protected void updateItem(ImageData item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    try {
                        ImageView imv = new ImageView(item.readImageFromArray());
                        setGraphic(imv);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        TableColumn tbcdanhgia_hh = new TableColumn("Đánh Giá Hàng Hóa");
        tbcdanhgia_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Float>("danhgia_hh"));
        tbv_data.setItems(oblhanghoa);
        tbv_data.getColumns().addAll(tbcid_hh, tbcten_hh, tbchinhanh_hh, tbcsoluong_hh, tbcgiatien_hh, tbcdanhgia_hh, tbcmota_hh);
    }

    public void taiDuLieuChoBangGioHang() {
        tbv_cart.getItems().clear();
        tbv_cart.getColumns().clear();
        ObservableList<HangHoa> oblgiohang = FXCollections.observableArrayList(Main.giohang);
        TableColumn tbcid_hh = new TableColumn("ID Hàng Hóa");
        tbcid_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Integer>("id_hh"));
        TableColumn tbcten_hh = new TableColumn("Tên Hàng Hóa");
        tbcten_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, String>("ten_hh"));
        TableColumn tbcsoluong_hh = new TableColumn("Số Lượng Hàng Hóa");
        tbcsoluong_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Integer>("soluong_hh"));
        TableColumn tbcgiatien_hh = new TableColumn("Giá Hàng Hóa");
        tbcgiatien_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, Integer>("giatien_hh"));
        TableColumn tbcmota_hh = new TableColumn("Mô Tả Hàng Hóa");
        tbcmota_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, String>("mota_hh"));
        TableColumn tbchinhanh_hh = new TableColumn("Hình Ảnh Hàng Hóa");
        tbchinhanh_hh.setCellValueFactory(new PropertyValueFactory<HangHoa, ImageData>("hinhanh_hh2"));
        tbchinhanh_hh.setCellFactory(p -> new TableCell<HangHoa, ImageData>() {
            @Override
            protected void updateItem(ImageData item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    try {
                        ImageView imv = new ImageView(item.readImageFromArray());
                        setGraphic(imv);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tbv_cart.setItems(oblgiohang);
        tbv_cart.getColumns().addAll(tbcid_hh, tbcten_hh, tbchinhanh_hh, tbcmota_hh, tbcgiatien_hh, tbcsoluong_hh);
    }

    public void taiDuLieuChoBangDanhMuc() {
        tbv_dm.getItems().clear();
        tbv_dm.getColumns().clear();
        ObservableList<DanhMucHang> obldm = FXCollections.observableArrayList(Main.temp_data.getDanhmuchang());
        TableColumn tbcid_dm = new TableColumn("ID Danh Mục");
        tbcid_dm.setCellValueFactory(new PropertyValueFactory<DanhMucHang, Integer>("id_dm"));
        TableColumn tbcten_dm = new TableColumn("Tên Danh Mục");
        tbcten_dm.setCellValueFactory(new PropertyValueFactory<DanhMucHang, String>("ten_dm"));
        TableColumn tbcbieutuong_dm = new TableColumn("Biểu Tượng Danh Mục");
        tbcbieutuong_dm.setCellValueFactory(new PropertyValueFactory<DanhMucHang, ImageData>("hinhanh"));
        tbcbieutuong_dm.setCellFactory(p -> new TableCell<DanhMucHang, ImageData>() {
            @Override
            protected void updateItem(ImageData item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    try {
                        ImageView imv = new ImageView(item.readImageFromArray());
                        imv.setFitHeight(100);
                        imv.setPreserveRatio(true);
                        setGraphic(imv);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tbv_dm.setItems(obldm);
        tbv_dm.getColumns().addAll(tbcid_dm, tbcten_dm, tbcbieutuong_dm);
    }

    public void taiDuLieuChoBangNhaCC() {
        tbv_ncc.getItems().clear();
        tbv_ncc.getColumns().clear();
        ObservableList<NhaCC> oblncc = FXCollections.observableArrayList(Main.temp_data.getNhacc());
        TableColumn tbcid_ncc = new TableColumn("ID Nhà Cung Cấp");
        tbcid_ncc.setCellValueFactory(new PropertyValueFactory<NhaCC, Integer>("id_ncc"));
        TableColumn tbcten_ncc = new TableColumn("Tên Nhà Cung Cấp");
        tbcten_ncc.setCellValueFactory(new PropertyValueFactory<NhaCC, String>("ten_ncc"));
        TableColumn tbcquocgia_ncc = new TableColumn("Quốc Gia Nhà Cung Cấp");
        tbcquocgia_ncc.setCellValueFactory(new PropertyValueFactory<NhaCC, String>("quocgia_ncc"));
        TableColumn tbcbieutuong_ncc = new TableColumn("Biểu Tượng Nhà Cung Cấp");
        tbcbieutuong_ncc.setCellValueFactory(new PropertyValueFactory<NhaCC, ImageView>("hinhanh"));
        tbcbieutuong_ncc.setCellFactory(p -> new TableCell<NhaCC, ImageData>() {
            @Override
            protected void updateItem(ImageData item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    try {
                        ImageView imv = new ImageView(item.readImageFromArray());
                        setGraphic(imv);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tbv_ncc.setItems(oblncc);
        tbv_ncc.getColumns().addAll(tbcid_ncc, tbcten_ncc, tbcquocgia_ncc, tbcbieutuong_ncc);
    }

    public void taiDuLieuChoBangNhanVien() {
        tbv_nhanvien.getItems().clear();
        tbv_nhanvien.getColumns().clear();
        ObservableList<NhanVien> oblnv = FXCollections.observableArrayList(Main.temp_data.getNhanvien());
        TableColumn tbcid_nv = new TableColumn("ID");
        tbcid_nv.setCellValueFactory(new PropertyValueFactory<NhaCC, Integer>("id_nv"));
        TableColumn tbchoten_nv = new TableColumn("Họ Tên Nhân Viên");
        tbchoten_nv.setCellValueFactory(new PropertyValueFactory<NhaCC, String>("hoten_nv"));
        TableColumn tbcsdt_nv = new TableColumn("Số Điện Thoại");
        tbcsdt_nv.setCellValueFactory(new PropertyValueFactory<NhaCC, String>("sdt_nv"));
        /*TableColumn tbcmk_nv = new TableColumn("Mật Khẩu");
        tbcmk_nv.setCellValueFactory(new PropertyValueFactory<NhaCC, String>("mk_nv"));*/
        TableColumn tbcgt_nv = new TableColumn("Giới Tính");
        tbcgt_nv.setCellValueFactory(new PropertyValueFactory<NhaCC, ImageView>("gt_nv"));
        tbv_nhanvien.setItems(oblnv);
        tbv_nhanvien.getColumns().addAll(tbcid_nv, tbchoten_nv, tbcsdt_nv, tbcgt_nv);
    }

    public void tbv_dataDblClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() >= 2) {
            tbv_dataSua(null);
        }
    }

    public void tbv_dataSua(ActionEvent actionEvent) throws IOException {
        if (tbv_data.getSelectionModel().getSelectedIndex() >= 0)
            Main.openOption("/sample/views/quanlyhanghoa.fxml", "/sample/styles/quanlyhanghoa.css",
                    "Quản Lý Hàng Hóa", 425, 550, tbv_data.getSelectionModel().getSelectedItem()).show();
        else {
            showAlert("Lỗi thực hiện sửa dữ liệu",
                    "Chưa chọn dữ liệu: nhấp chuột phải lên dòng dữ liệu cần sửa > chọn 'Sửa Dòng Được Chọn'.\nHOẶC\nDữ liệu trống: mở file dữ liệu hoặc tạo mới dữ liệu",
                    Alert.AlertType.ERROR);
        }
    }

    public static ButtonType showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().getChildren().get(2);
        Button btok = (Button) buttonBar.getButtons().get(0);
        btok.setText("Đồng ý");
        if (buttonBar.getButtons().size() == 2) {
            Button btcancel = (Button) buttonBar.getButtons().get(1);
            btcancel.setText("Hủy bỏ");
        }
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Controller.class.getResourceAsStream("/sample/medias/icon.png")));
        alert.showAndWait();
        return alert.getResult();
    }

    public void btthemnhanvienClick(ActionEvent actionEvent) throws IOException {
        Main.openOption("/sample/views/quanlynhanvien.fxml", "/sample/styles/quanlynhanvien.css",
                "Quản Lý Nhân Viên", 425, 280, null).show();
    }

    public void mnithemvaogiohangClick(ActionEvent actionEvent) throws IOException, CloneNotSupportedException {
        HangHoa hh = new HangHoa(tbv_data.getSelectionModel().getSelectedItem());
        hh.setSoluong_hh(1);
        Main.giohang.add(hh);
        Controller.showAlert("Giỏ Hàng", "Hàng bạn chọn đã được thêm vào giỏ hàng", Alert.AlertType.INFORMATION);
        daluuchua = false;
    }

    public void mnixoahanghoaClick(ActionEvent actionEvent) {
        HangHoa hh = tbv_data.getSelectionModel().getSelectedItem();
        if (hh == null) {
            showAlert("Xóa Hàng ", "Lỗi! Hãy Chọn Hàng Để Xóa", Alert.AlertType.ERROR);
        } else {
            ButtonType bttype = showAlert("Xóa ", "Bạn có muốn xóa  được chọn?",
                    Alert.AlertType.CONFIRMATION);
            if (bttype == ButtonType.OK) {
                Main.temp_data.getHanghoa().remove(Main.temp_data.getHangHoaAt(hh.getId_hh()));
                showAlert("Xóa ", "Hàng được chọn đã xóa thành công", Alert.AlertType.INFORMATION);
                daluuchua = false;
            }
        }
    }

    public void mnithaydoisoluonghangClick(ActionEvent actionEvent) {
        try {
            TextInputDialog dialog = new TextInputDialog(tbv_cart.getSelectionModel().getSelectedItem().getSoluong_hh() + "");
            dialog.setTitle("Thay Đổi Số Lượng Hàng");
            dialog.setContentText("Nhập số lượng hàng cần mua:");
            dialog.setHeaderText(null);
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Controller.class.getResourceAsStream("/sample/medias/icon.png")));
            dialog.showAndWait();
            int soluongmoi = Integer.parseInt(dialog.getResult());
            tbv_cart.getSelectionModel().getSelectedItem().setSoluong_hh(soluongmoi);
            taiLaiDuLieu(6);
            daluuchua = false;
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void tbv_cartDblClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2)
            mnithaydoisoluonghangClick(null);
    }

    public void mniloaibokhoigiohangClick(ActionEvent actionEvent) {
        if (tbv_cart.getSelectionModel().getSelectedIndex() >= 0) {
            ButtonType muonxoa = showAlert("Xoá giỏ hàng", "Bạn muốn xóa giỏ hàng này? Lưu ý rằng, giỏ hàng bị xóa sẽ không thể khôi phục lại!", Alert.AlertType.CONFIRMATION);
            if (muonxoa == ButtonType.OK) {
                Main.giohang.remove(tbv_cart.getSelectionModel().getSelectedItem());
                tbv_cart.getItems().remove(tbv_cart.getSelectionModel().getSelectedItem());
                showAlert("Xóa ", "Hàng được chọn đã xóa thành công", Alert.AlertType.INFORMATION);
                taiLaiDuLieu(6);
                daluuchua = false;
            }
        } else {
            showAlert("Lỗi xóa giỏ hàng", "Bạn cần chọn giỏ hàng cần xóa !", Alert.AlertType.ERROR);
        }
    }

    public void btthemkhachhangClick(ActionEvent actionEvent) throws IOException {
        Main.openOption("/sample/views/quanlykhachhang.fxml", "/sample/styles/quanlykhachhang.css",
                "Quản Lý Khách Hàng", 425, 380, null).show();
    }

    public void tbv_khachhangDblClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            if (tbv_khachhang.getSelectionModel().getSelectedIndex() >= 0)
                Main.openOption("/sample/views/quanlykhachhang.fxml", "/sample/styles/quanlykhachhang.css",
                        "Quản Lý Khách Hàng", 425, 380, tbv_khachhang.getSelectionModel().getSelectedItem()).show();
            else {
                showAlert("Lỗi thực hiện sửa dữ liệu",
                        "Chưa chọn dữ liệu: nhấp đôi chuột lên dòng dữ liệu cần sửa.\nHOẶC\nDữ liệu trống: mở file dữ liệu hoặc tạo mới dữ liệu",
                        Alert.AlertType.ERROR);
            }
        }
    }

    String msg = "";

    public void btthemmoidonhangClick(ActionEvent actionEvent) {
        try {
            if (tbv_cart.getItems().size() > 0) {
                tabpane.getSelectionModel().select(tabgiohang);
                ArrayList<Integer> ids_hang = new ArrayList();
                ArrayList<Integer> soluongs_hang = new ArrayList();
                ArrayList<Integer> dongias_hang = new ArrayList();
                int id_nv = 0;
                if (DangNhapController.aidangdangnhap.compareToIgnoreCase("administrator") != 0)
                    id_nv = Integer.parseInt(DangNhapController.aidangdangnhap);
                Main.openOption("/sample/views/chonkhachhang.fxml", "/sample/styles/chonkhachhang.css",
                        "Khách Hàng Đang Mua Hàng", 475, 380, null).showAndWait();
                int id_kh = ChonKhachHang.khachhanghientai.getId_kh();

                //Chuyển các mặt hàng trong giỏ hàng vào đơn hàng
                for (HangHoa hh : Main.giohang) {
                    ids_hang.add(hh.getId_hh());
                    soluongs_hang.add(hh.getSoluong_hh());
                    dongias_hang.add(hh.getGiatien_hh());
                }
                if (kiemTraHangCon(ids_hang, soluongs_hang)) {
                    //Tạo mới đơn hàng từ dữ liệu lấy được ở trên
                    DonHang dh = new DonHang(Main.temp_data.getNextIDDonHang(), id_kh, ids_hang, soluongs_hang, dongias_hang, LocalDateTime.now(), id_nv);
                    Main.temp_data.getDonhang().add(dh);
                    //System.out.println(dh.toString());
                    //Làm sạch dữ liệu liên quan đến đơn hàng
                    ChonKhachHang.khachhanghientai = null;
                    tbv_cart.getItems().clear();
                    Main.giohang.clear();
                    tabpane.getSelectionModel().select(tabhoadon);
                    showAlert("Tạo Đơn Hàng", "Đơn hàng đã được tạo thành công và lưu vào trong thẻ Hóa đơn!\nĐể xuất hóa đơn, xin vui lòng 'Nhấp chuột phải lên hóa đơn > chọn In hóa đơn'.", Alert.AlertType.INFORMATION);
                    capNhatSoLuongHang(ids_hang, soluongs_hang);
                    taiLaiDuLieu(5);
                    taiLaiDuLieu(0);
                } else {
                    showAlert("Mua Hàng", "Các mặt hàng sau không đủ số lượng để bán : " + msg, Alert.AlertType.WARNING);
                }
            } else {
                showAlert("Xuất Hóa Đơn", "Giỏ hàng trống! Vui lòng thêm hàng vào giỏ trước!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
        }
    }


    private boolean kiemTraHangCon(ArrayList<Integer> ids_hang, ArrayList<Integer> soluongs_hang) {
        msg = "";
        boolean result = true;
        for (int i = 0; i < ids_hang.size(); i++) {
            if (Main.temp_data.getHangHoaAt(ids_hang.get(i)).getSoluong_hh() < soluongs_hang.get(i)) {
                result = false;
                msg += Main.temp_data.getHangHoaAt(ids_hang.get(i)).getTen_hh() + ", ";
            }
        }
        return result;
    }

    private void capNhatSoLuongHang(ArrayList<Integer> ids_hang, ArrayList<Integer> soluongs_hang) {
        for (int i = 0; i < ids_hang.size(); i++) {
            Main.temp_data.getHangHoaAt(ids_hang.get(i))
                    .setSoluong_hh(Main.temp_data.getHangHoaAt(ids_hang.get(i)).getSoluong_hh() - soluongs_hang.get(i));
        }
    }

    public void mniinClick(ActionEvent actionEvent) throws IOException {
        if (tbv_hoadon.getSelectionModel().getSelectedIndex() >= 0)
            Main.openOption("/sample/views/inhoadon.fxml", "/sample/styles/inhoadon.css",
                    "In Hóa Đơn", 825, 680, tbv_hoadon.getSelectionModel().getSelectedItem()).show();
        else {
            showAlert("Lỗi in hóa đơn", "Bạn cần chọn hóa đơn cần in: Nhấp đôi chuột lên hóa đơn cần in!", Alert.AlertType.ERROR);
        }
    }

    public void tbv_hoadonDblClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() >= 2) {
            mniinClick(null);
        }
    }

    public void mnixoahoadonClick(ActionEvent actionEvent) {
        if (tbv_hoadon.getSelectionModel().getSelectedIndex() >= 0) {
            ButtonType muonxoa = showAlert("Xóa Hóa Đơn", "Bạn muốn xóa hóa đơn này? Lưu ý rằng, hóa đơn bị xóa sẽ không thể khôi phục lại!", Alert.AlertType.CONFIRMATION);
            if (muonxoa == ButtonType.OK) {
                Main.temp_data.getDonhang().remove(tbv_hoadon.getSelectionModel().getSelectedItem());
                tbv_hoadon.getItems().remove(tbv_hoadon.getSelectionModel().getSelectedItem());
                taiLaiDuLieu(5);
                daluuchua = false;
            }
        } else {
            showAlert("Lỗi xóa hóa đơn", "Bạn cần chọn hóa đơn cần xóa !", Alert.AlertType.ERROR);
        }
    }

    public void mnithemhdClick(ActionEvent actionEvent) {
        btthemmoidonhangClick(null);
    }

    public void mnithongtinClick(ActionEvent actionEvent) throws IOException {
        Main.openOption("/sample/views/gioithieuchuongtrinh.fxml", "/sample/styles/gioithieuchuongtrinh.css", "Chương Trình Quản Lý Bán Hàng", 450, 250, null).show();
    }

    public void mnithoatchuongtrinhClick(ActionEvent actionEvent) {
        System.exit(0);
    }

    //làm tương tự cái khác
    public void mnixoadanhmucClick(ActionEvent actionEvent) {
        DanhMucHang dm = tbv_dm.getSelectionModel().getSelectedItem();
        if (dm == null) {
            showAlert("Xóa Danh Mục", "Lỗi! Hãy Chọn Danh Mục Để Xóa", Alert.AlertType.ERROR);
        } else {
            ButtonType bttype = showAlert("Xóa Danh Mục", "Bạn có muốn xóa danh mục được chọn?",
                    Alert.AlertType.CONFIRMATION);
            if (bttype == ButtonType.OK) {
                Main.temp_data.getDanhmuchang().remove(Main.temp_data.getDanhMucAt(dm.getId_dm()));
                showAlert("Xóa Danh Mục", "Danh Mục được chọn đã xóa thành công", Alert.AlertType.INFORMATION);
                daluuchua = false;
            }
        }
    }

    //làm tương tự rồi vào file fxml
    public void mnisuadanhmucClick(ActionEvent actionEvent) throws IOException {
        DanhMucHang dm = tbv_dm.getSelectionModel().getSelectedItem();
        if (dm == null) {
            showAlert("Cập Nhật Danh Mục", "Lỗi! Hãy Chọn Danh Mục Để cập nhật mới", Alert.AlertType.ERROR);
        } else {
            Main.openOption("/sample/views/quanlydanhmuc.fxml", "/sample/styles/quanlydanhmuc.css",
                    "Quán Lý Danh Mục", 425, 260, dm).show();
        }
    }

    public void mnisuanccClick(ActionEvent actionEvent) throws IOException {
        NhaCC ncc = tbv_ncc.getSelectionModel().getSelectedItem();
        if (ncc == null) {
            showAlert("Cập Nhật Nhà Cung Cấp", "Lỗi! Hãy chọn nhà cung cấp để cập nhật mới", Alert.AlertType.ERROR);
        } else {
            Main.openOption("/sample/views/quanlynhacungcap.fxml", "/sample/styles/quanlynhacungcap.css", "Quản Lý Nhà Cung Cấp", 425, 310, ncc).show();
        }

    }

    public void mnixoanccClick(ActionEvent actionEvent) {
        NhaCC ncc = tbv_ncc.getSelectionModel().getSelectedItem();
        if (ncc == null) {
            showAlert("Xóa Nhà Cung Cấp", "Lỗi! Hãy Chọn Nhà Cung Cấp Để Xóa", Alert.AlertType.INFORMATION);
        } else {
            ButtonType bttype = showAlert("Xóa Nhà Cung Cấp ", "Bạn có muốn xóa nhà cung cấp được chọn?",
                    Alert.AlertType.CONFIRMATION);
            if (bttype == ButtonType.OK) {
                Main.temp_data.getNhacc().remove(Main.temp_data.getNhaCCAt(ncc.getId_ncc()));
                showAlert("Xóa Nhà Cung Cấp", "Nhà Cung Cấp được chon đã xóa thành công", Alert.AlertType.INFORMATION);
                daluuchua = false;
            }
        }
    }

    public void mnixoanvClick(ActionEvent actionEvent) {
        NhanVien nv = tbv_nhanvien.getSelectionModel().getSelectedItem();
        if (nv == null) {
            showAlert("Xóa Nhân Viên", "Lỗi! Hãy Chọn Nhân Viên Để Xóa", Alert.AlertType.ERROR);
        } else {
            ButtonType bttype = showAlert("Xóa Nhân Viên", "Bạn có muốn xóa nhân viên được chọn?",
                    Alert.AlertType.CONFIRMATION);
            if (bttype == ButtonType.OK) {
                Main.temp_data.getNhanvien().remove(Main.temp_data.getNhanViengAt(nv.getId_nv()));
                showAlert("Xóa Nhân Viên", "Nhân Viên được chọn đã xóa thành công", Alert.AlertType.INFORMATION);
                daluuchua = false;
            }
        }
    }

    public void mnisuanvClick(ActionEvent actionEvent) throws IOException {
        NhanVien nv = tbv_nhanvien.getSelectionModel().getSelectedItem();
        if (nv == null) {
            showAlert("Cập Nhật Nhân Viên", "Lỗi! Hãy Chọn Nhân Viên Để Cập Nhật Mới", Alert.AlertType.ERROR);
        } else {
            Main.openOption("/sample/views/quanlynhanvien.fxml", "/sample/Styles/quanlynhanvien.css", "Quản Lý Nhân Viên",
                    420, 280, nv).show();
        }
    }

    public void mnixoakhClick(ActionEvent actionEvent) {
        KhachHang kh = tbv_khachhang.getSelectionModel().getSelectedItem();
        if (kh == null) {
            showAlert("Xóa Khách Hàng", "Lỗi! Hãy Chọn Khách Hàng Để Xóa", Alert.AlertType.ERROR);
        } else {
            ButtonType bttype = showAlert("Xóa Khách Hàng", "Bạn có muốn xóa Khách Hàng được chọn?",
                    Alert.AlertType.CONFIRMATION);
            if (bttype == ButtonType.OK) {
                Main.temp_data.getKhachang().remove(Main.temp_data.getKhachHangAt(kh.getId_kh()));
                showAlert("Xóa Khách Hàng", "Khách Hàng được chọn đã xóa thành công", Alert.AlertType.INFORMATION);
                daluuchua = false;
            }
        }
    }

    public void mnisuakhClick(ActionEvent actionEvent) throws IOException {
        KhachHang kh = tbv_khachhang.getSelectionModel().getSelectedItem();
        if (kh == null) {
            showAlert("Cập Nhật Khách Hàng", "Lỗi! Hãy Chọn Khách Hàng Để Cập Nhật Mới", Alert.AlertType.ERROR);
        } else {
            Main.openOption("/sample/views/quanlykhachhang.fxml", "/sample/Styles/quanlykhachhang.css", "Quản Lý Khách Hàng",
                    425, 380, kh).show();
        }
    }

    public void bttimkiemClick(ActionEvent actionEvent) {
        String tukhoa = tftukhoa.getText();
        String giatu = tfgiatu.getText();
        String giaden = tfgiaden.getText();
        DanhMucHang dm = cbdm.getSelectionModel().getSelectedItem();
        NhaCC ncc = cbncc.getSelectionModel().getSelectedItem();
        ketquatimkiem = Main.temp_data.timKiemHangHoa(tukhoa, giatu, giaden, dm, ncc);
        taiLaiDuLieu(0);
    }

    public void btlamlaiClick(ActionEvent actionEvent) {
        tftukhoa.clear();
        tfgiaden.clear();
        tfgiatu.clear();
        cbdm.getSelectionModel().select(null);
        cbncc.getSelectionModel().select(null);
        ketquatimkiem = Main.temp_data.getHanghoa();
        taiLaiDuLieu(0);
    }
}
