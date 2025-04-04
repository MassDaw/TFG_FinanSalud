// Componente para la caja de carga de archivos
function UploadBox({ onFileUpload }) {
    const fileInputRef = React.useRef(null);
    const [isDragging, setIsDragging] = React.useState(false);

    const handleDragOver = (e) => {
        e.preventDefault();
        setIsDragging(true);
    };

    const handleDragLeave = () => {
        setIsDragging(false);
    };

    const handleDrop = (e) => {
        e.preventDefault();
        setIsDragging(false);

        if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            const event = {
                target: {
                    files: e.dataTransfer.files
                }
            };

            onFileUpload(event);
        }
    };

    const triggerFileInput = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    // Inicializar los iconos de Lucide
    React.useEffect(() => {
        lucide.createIcons();
    }, []);

    return (
        <div
            className={`border-2 border-dashed rounded-lg p-8 flex flex-col items-center justify-center ${
                isDragging ? "border-blue-500 bg-blue-50" : "border-blue-200 bg-blue-50"
            }`}
            onDragOver={handleDragOver}
            onDragLeave={handleDragLeave}
            onDrop={handleDrop}
        >
            <div className="bg-blue-100 p-8 rounded-full mb-6">
                <div className="w-16 h-16 flex items-center justify-center border-2 border-blue-600 rounded-md">
                    <i data-lucide="upload" className="text-blue-600"></i>
                </div>
            </div>

            <div className="flex gap-4 mb-4">
                <button
                    onClick={() => triggerFileInput()}
                    className="bg-blue-400 text-white px-6 py-2 rounded-md flex items-center gap-2 hover:bg-blue-500 transition-colors"
                >
                    <i data-lucide="scan-line" className="w-4 h-4"></i>
                    Escanear ticket
                </button>

                <button
                    onClick={() => triggerFileInput()}
                    className="bg-blue-400 text-white px-6 py-2 rounded-md flex items-center gap-2 hover:bg-blue-500 transition-colors"
                >
                    <i data-lucide="plus" className="w-4 h-4"></i>
                    Añadir manualmente
                </button>
            </div>

            <p className="text-blue-400">Hasta 10MB</p>

            <input
                type="file"
                ref={fileInputRef}
                onChange={onFileUpload}
                className="hidden"
                multiple
            />
        </div>
    );
}