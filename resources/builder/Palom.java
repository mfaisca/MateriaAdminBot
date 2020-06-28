package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Palom extends _Default{
	public Palom() {
		super("Palom", "parom");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3163};
				break;
			case HP:
				ids = new int[]{9678};
				break;
			case S1:
				ids = new int[]{3161, 3159, 3160};
				break;
			case S2:
				ids = new int[]{3170, 3167, 3168, 3169};
				break;
			case EX:
				ids = new int[]{2839};
				break;
			case AA:
				ids = new int[]{5354};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}