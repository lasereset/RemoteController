package com.dmrjkj.remotecontroller.bluetooth.parse;

import com.clj.fastble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinchen on 19-5-27.
 */
public class ParseEngine {
    private ParseCallBack callBack;
    private List<ParseCore> parseCores;
    private String parseFrame = "";

    public ParseEngine(ParseCore paramParseCore, ParseCallBack paramParseCallBack) {
        this.parseCores = new ArrayList();
        this.parseCores.add(paramParseCore);
        this.callBack = paramParseCallBack;
    }

    public ParseEngine(List<ParseCore> paramList, ParseCallBack paramParseCallBack) {
        this.parseCores = paramList;
        this.callBack = paramParseCallBack;
    }

    public void execParse(String paramString) {
        this.parseFrame = paramString.toUpperCase();
        for (int i=0; i < parseCores.size(); i++) {
            Result result = this.parseCores.get(i).parse(this.parseFrame);
            if (result.getCode() == -1 || result.getCode() == 100) {
                continue;
            }
            if ((result.getCode() == 200) && (this.callBack != null)) {
                this.parseFrame = result.getRemainFrame();
                ParseResult parseResult = new ParseResult(result.getStringFrame(), result.getFrameObj());
                this.callBack.onResult(parseResult);
            }
        }
    }

    public void execParse(byte[] paramArrayOfByte) {
        execParse(HexUtil.encodeHexStr(paramArrayOfByte));
    }
}
