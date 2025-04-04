// Componente principal
import UploadBox from "../components/upload-box";
import FileTable from "../components/file-table";

function App() {
    const [files, setFiles] = React.useState([
        { fecha: "03 Abr", nombre: "nombreArchivo.png", autor: "@Paolo", tamano: "190KB" },
        { fecha: "03 Abr", nombre: "nombreArchivo.png", autor: "@Paolo", tamano: "190KB" },
        { fecha: "03 Abr", nombre: "nombreArchivo.png", autor: "@Paolo", tamano: "190KB" },
        { fecha: "03 Abr", nombre: "nombreArchivo.png", autor: "@Paolo", tamano: "190KB" },
        { fecha: "03 Abr", nombre: "nombreArchivo.png", autor: "@Paolo", tamano: "190KB" },
        { fecha: "03 Abr", nombre: "nombreArchivo.png", autor: "@Paolo", tamano: "190KB" },
    ]);

    const handleFileUpload = (e) => {
        if (e.target.files && e.target.files.length > 0) {
            const formData = new FormData();

            Array.from(e.target.files).forEach(file => {
                formData.append("files", file);
            });

            // Llamada a tu API de Spring Boot
            fetch('http://localhost:8080/api/files/upload', {
                method: 'POST',
                body: formData,
            })
                .then(response => response.json())
                .then(data => {
                    // Si tu backend devuelve los datos de los archivos
                    const newFiles = data.map(fileData => ({
                        fecha: new Date(fileData.uploadDate).toLocaleDateString('es', { day: '2-digit', month: 'short' }),
                        nombre: fileData.fileName,
                        autor: fileData.uploadedBy,
                        tamano: `${Math.round(fileData.size / 1024)}KB`
                    }));

                    setFiles([...newFiles, ...files]);
                })
                .catch(error => {
                    console.error('Error al subir archivos:', error);
                    // Fallback en caso de error: mostrar los archivos localmente
                    const newFiles = Array.from(e.target.files).map(file => ({
                        fecha: new Date().toLocaleDateString('es', { day: '2-digit', month: 'short' }),
                        nombre: file.name,
                        autor: "@Paolo",
                        tamano: `${Math.round(file.size / 1024)}KB`
                    }));

                    setFiles([...newFiles, ...files]);
                });
        }
    };

    return (
        <main className="max-w-4xl mx-auto p-4">
            <UploadBox onFileUpload={handleFileUpload} />
            <div className="mt-8">
                <h2 className="text-xl font-medium text-gray-700 mb-4">Archivos subidos</h2>
                <FileTable files={files} />
            </div>
        </main>
    );
}

// Renderizar la aplicación en el div con id "root"
ReactDOM.render(<App />, document.getElementById('root'));