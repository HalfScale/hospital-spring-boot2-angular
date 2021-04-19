export const GlobalVariable = Object.freeze({
    BASE_API_URL: 'http://localhost:8070/hospital',
    DEFAULT_PROFILE_IMG: 'https://i.ibb.co/ZVFsg37/default.png',
    
});

export const FileUtil = Object.freeze({
    dataURItoBlob: (dataURI: any)=> {
        // convert base64 to raw binary data held in a string
        // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
        
        const byteString = atob(dataURI.split(',')[1]);

        //separate out the mime component
        const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

        // write the bytes of the string to an ArrayBuffer
        const arrayBuffer = new ArrayBuffer(byteString.length);
        const ia = new Uint8Array(arrayBuffer);
        for(var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        //Old Code
        //write the ArrayBuffer to a blob, and you're done
        //var bb = new BlobBuilder();
        //bb.append(ab);
        //return bb.getBlob(mimeString);

        //New Code
        return new Blob([arrayBuffer], {type: mimeString});
    }
});